package com.Carrot.CR_Controller;

import com.Carrot.CR_Model.SaleProduct;
import com.Carrot.CR_Service.JwtService;
import com.Carrot.CR_Service.SaleProductService;
import com.Carrot.Jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class SaleProductController{

    public static final int SHOW_COUNT = 10;

    private final SaleProductService saleProductService;
    private final JwtTokenProvider jwtTokenProvider;

    public SaleProductController(SaleProductService saleProductService, JwtTokenProvider jwtTokenProvider) {
        this.saleProductService = saleProductService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String getUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.getAccessToken(request);
        return jwtTokenProvider.getUserPk(token);
    }

    @PostMapping("/p1/write")
    public SaleProduct write(HttpServletRequest request, @RequestBody Map<String, String> post) {
        SaleProduct saleProduct = SaleProduct.builder()
                .id(getUserInfo(request))
                .title(post.get("title"))
                .category(post.get("category"))
                .price(Integer.parseInt(post.get("price")))
                .content(post.get("content"))
                .image(post.get("image"))
                .status(post.get("status"))
                .love(0).build();

        return saleProductService.write(saleProduct);
    }

    @PostMapping("/p1/update")
    public SaleProduct update(HttpServletRequest request, @RequestBody Map<String, String> post) {
        SaleProduct saleProduct = SaleProduct.builder()
                .id(getUserInfo(request))
                .title(post.get("title"))
                .category(post.get("category"))
                .price(Integer.parseInt(post.get("price")))
                .content(post.get("content"))
                .image(post.get("image"))
                .status(post.get("status")).build();
        return saleProductService.update(saleProduct);
    }

    @GetMapping("/list/{page}")
    public List<SaleProduct> show(@PathVariable(name = "page") int pageNum) {
        //무한 스크롤
        int limit = getLimitCnt(pageNum);
        int offset = limit - 10;
        return saleProductService.getPage(limit, offset);

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
