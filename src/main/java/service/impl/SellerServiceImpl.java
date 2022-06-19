package service.impl;

import model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.ISellerRepository;
import service.ISellerService;

import java.util.Optional;

@Service
public class SellerServiceImpl implements ISellerService {
    @Autowired
    ISellerRepository sellerRepository;

    @Override
    public Page<Seller> findAll(Pageable pageable) {
        return sellerRepository.findAll(pageable);
    }

    @Override
    public Optional<Seller> findById(Long id) {
        return sellerRepository.findById(id);
    }

    @Override
    public void save(Seller seller) {
        sellerRepository.save(seller);
    }

    @Override
    public void remove(Long id) {
        sellerRepository.deleteById(id);
    }
}
