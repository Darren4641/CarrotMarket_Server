package com.Carrot.CR_Controller;

import com.Carrot.CR_Model.Photo_SaleProduct;
import com.Carrot.CR_Model.SaleProduct;
import com.Carrot.CR_Service.FileUploadDownloadService;
import com.Carrot.CR_Service.SaleProductService;
import com.Carrot.ErrorHandler.ApiResponse;
import com.Carrot.Jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class SaleProductController{

    public static final int SHOW_COUNT = 10;

    private final SaleProductService saleProductService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private final FileUploadDownloadService fileUploadDownloadService;

    public SaleProductController(SaleProductService saleProductService, JwtTokenProvider jwtTokenProvider, FileUploadDownloadService fileUploadDownloadService) {
        this.saleProductService = saleProductService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.fileUploadDownloadService = fileUploadDownloadService;
    }

    private String getUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.getAccessToken(request);
        return jwtTokenProvider.getUserPk(token);
    }

    //게시물 작성
    @PostMapping(value = "/p1/write", consumes = { MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ApiResponse write(HttpServletRequest request, @RequestPart Map<String, String> post, @RequestPart(required = false) MultipartFile[] files) {
        String id = getUserInfo(request);

        SaleProduct saleProduct = SaleProduct.builder()
                .id(id)
                .title(post.get("title"))
                .category(post.get("category"))
                .price(Integer.parseInt(post.get("price")))
                .content(post.get("content"))
                .status(post.get("status")).build();

        SaleProduct sale = saleProductService.write(saleProduct);
        if(files != null) {
            List<Photo_SaleProduct> photo_saleProductList = Arrays.asList(files)
                    .stream()
                    .map(file -> fileUploadDownloadService.storeFile(file, "saleProduct", id, sale.getPostId()))
                    .collect(Collectors.toList());
        }else {
            fileUploadDownloadService.storeFile(null, "saleProduct", id, sale.getPostId());
        }
        return saleProductService.findByIdWithFileAndLike(sale.getPostId());
    }

    //게시물 수정
    @PostMapping(value = "/p1/update/{postId}", consumes = { MediaType.ALL_VALUE, MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ApiResponse update(HttpServletRequest request, @PathVariable(name = "postId") int postId, @RequestPart Map<String, String> post, @RequestPart(required = false) MultipartFile[] files) {
        String id = getUserInfo(request);
        SaleProduct origin_saleProduct = saleProductService.findSaleProduct(postId);
        SaleProduct saleProduct = SaleProduct.builder()
                .postId(postId)
                .id(id)
                .title(post.get("title"))
                .category(post.get("category"))
                .price(Integer.parseInt(post.get("price")))
                .content(post.get("content"))
                .status(post.get("status"))
                .createDate(origin_saleProduct.getCreateDate())
                .updateDate(origin_saleProduct.getUpdateDate()).build();

        saleProductService.update(saleProduct);
        if(files != null) {
            List<Photo_SaleProduct> photo_saleProductList = Arrays.asList(files)
                    .stream()
                    .map(file -> fileUploadDownloadService.updateFile(file, "saleProduct", id, postId))
                    .collect(Collectors.toList());
        }else {
            fileUploadDownloadService.updateFile(null, "saleProduct", id, postId);
        }

        return saleProductService.findByIdWithFileAndLike(postId);
    }


    @GetMapping(value = "/p1/like/{postId}")
    public void pushLike(HttpServletRequest request, @PathVariable(name = "postId") int postId) {
        String id = getUserInfo(request);
        saleProductService.pushLike(postId, id);
        System.out.println("Like");
    }

    //게시물 10개씩 보기
    @GetMapping("/list/{page}")
    public ApiResponse show(@PathVariable(name = "page") int pageNum) {
        //무한 스크롤
        int limit = getLimitCnt(pageNum);
        int offset = limit - 10;
        return saleProductService.getPage(limit, offset, pageNum);

    }

    //게시물 보기
    @GetMapping("/view/{postId}")
    public ApiResponse getPost(@PathVariable(name = "postId") int postId) {
        return saleProductService.findByIdWithFileAndLike(postId);
    }

    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for(int i = 0; i <= pageNum; i++) {
            if(i != 0)
                limit += SHOW_COUNT;
        }
        return limit;
    }

}
