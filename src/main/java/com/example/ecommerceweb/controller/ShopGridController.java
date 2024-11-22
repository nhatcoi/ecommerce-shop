package com.example.ecommerceweb.controller;

import com.example.ecommerceweb.dto.CategoryDTO;
import com.example.ecommerceweb.dto.ProductDTO;
import com.example.ecommerceweb.entity.FlashSaleItem;
import com.example.ecommerceweb.entity.FlashSale;
import com.example.ecommerceweb.service.CategoryService;
import com.example.ecommerceweb.service.FlashSaleItemService;
import com.example.ecommerceweb.service.FlashSaleService;
import com.example.ecommerceweb.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ecommerceweb.util.Static.*;
import static com.example.ecommerceweb.util.DivideList.divideList;

@Slf4j
@Controller
@RequestMapping("/shop-grid")
@RequiredArgsConstructor
public class ShopGridController {
    private final CategoryService categoryService;
    private final FlashSaleItemService flashSaleItemService;
    private final FlashSaleService flashSaleService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public String shopGrid(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
        FlashSale currentFlashSale = flashSaleService.getCurrentFlashSale() != null ? flashSaleService.getCurrentFlashSale() : new FlashSale();
        List<FlashSaleItem> flashSaleItems = flashSaleItemService.getProductsInFlashSale(currentFlashSale.getId());

        List<List<ProductDTO>> latestProducts = divideList(
                productService.getLatestProducts(LATEST_LIMIT).stream()
                        .map(product -> modelMapper.map(product, ProductDTO.class))
                        .collect(Collectors.toList()),
                PAGE_SLIDE
        );

        model.addAttribute("categories", categories);
        model.addAttribute("flashSaleItems", flashSaleItems);
        model.addAttribute("flashSale", currentFlashSale);
        model.addAttribute("latestProducts", latestProducts);
        return "shop-grid";
    }


    @GetMapping("/filterByPrice")
    @ResponseBody
    public List<ProductDTO> filterProducts(
            @RequestParam(value = "minamount", required = false) String minAmount,
            @RequestParam(value = "maxamount", required = false) String maxAmount) {
        int min, max;
        try {
            min = minAmount != null ? Integer.parseInt(minAmount) : 0;
            max = maxAmount != null ? Integer.parseInt(maxAmount) : Integer.MAX_VALUE;
        } catch (NumberFormatException e) {
            min = 0;
            max = Integer.MAX_VALUE;
        }
        return productService.getProductByPriceRange(min, max).stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
}