package controller;

import model.Product;
import model.ProductForm;
import model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.impl.ProductServiceImpl;
import service.impl.SellerServiceImpl;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Value("${file-upload}")
    private String fileUpload;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    SellerServiceImpl sellerService;

    @ModelAttribute("sellers")
    public Page<Seller> sellers(Pageable pageable) {
        return sellerService.findAll(pageable);
    }

    @GetMapping
    public ModelAndView showList(@PageableDefault(value = 3) Pageable pageable) {
        return new ModelAndView("/product/list", "products", productService.findAll(pageable));
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        return new ModelAndView("/product/create", "productForm", new ProductForm());
    }

    @PostMapping("/save")
    public ModelAndView saveProduct(@ModelAttribute ProductForm productForm, RedirectAttributes redirectAttributes) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), fileName,
                productForm.getSeller());
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        redirectAttributes.addFlashAttribute("success", "Created new product successfully !");
        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditForm(@PathVariable Long id) {
        return new ModelAndView("/product/edit", "product", productService.findById(id));
    }
}
