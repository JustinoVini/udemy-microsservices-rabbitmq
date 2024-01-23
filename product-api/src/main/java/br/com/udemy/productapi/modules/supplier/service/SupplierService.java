package br.com.udemy.productapi.modules.supplier.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.udemy.productapi.config.exception.ValidationException;
import br.com.udemy.productapi.modules.supplier.dto.SupplierRequest;
import br.com.udemy.productapi.modules.supplier.dto.SupplierResponse;
import br.com.udemy.productapi.modules.supplier.model.Supplier;
import br.com.udemy.productapi.modules.supplier.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier findById(Integer id) {
        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given ID."));
    }

    public SupplierResponse save(SupplierRequest request) {
        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    private void validateSupplierNameInformed(SupplierRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("The supplier name was not found");
        }
    }

}
