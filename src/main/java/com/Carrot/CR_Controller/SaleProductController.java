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

    public String getUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.getAccessToken(request);
        return jwtTokenProvider.getUserPk(token);
    }

    @PostMapping(value = "/p1/write", consumes = { MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ApiResponse write(HttpServletRequest request, @RequestPart Map<String, String> post, @RequestPart(required = false) MultipartFile[] files) {
        String id = getUserInfo(request);

        SaleProduct saleProduct = SaleProduct.builder()
                .id(id)
                .title(post.get("title"))
                .category(post.get("category"))
                .price(Integer.parseInt(post.get("price")))
                .content(post.get("content"))
                .status(post.get("status"))
                .love(0).build();

        SaleProduct sale = saleProductService.write(saleProduct);

        List<Photo_SaleProduct> photo_saleProductList = Arrays.asList(files)
                .stream()
                .map(file -> fileUploadDownloadService.storeFile(file, "saleProduct", id, sale.getPostId()))
                .collect(Collectors.toList());


        return saleProductService.findById(sale.getPostId());
    }

    @PostMapping("/p1/update")
    public SaleProduct update(HttpServletRequest request, @RequestBody Map<String, String> post) {
        SaleProduct saleProduct = SaleProduct.builder()
                .id(getUserInfo(request))
                .title(post.get("title"))
                .category(post.get("category"))
                .price(Integer.parseInt(post.get("price")))
                .content(post.get("content"))
                .status(post.get("status")).build();
        return saleProductService.update(saleProduct);
    }

    @GetMapping("/list/{page}")
    public ApiResponse show(@PathVariable(name = "page") int pageNum) {
        //무한 스크롤
        int limit = getLimitCnt(pageNum);
        int offset = limit - 10;
        return saleProductService.getPage(limit, offset, pageNum);

    }

    @GetMapping("/view/{postId}")
    public ApiResponse getPost(@PathVariable(name = "postId") int postId) {
        return saleProductService.findById(postId);
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
