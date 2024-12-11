package com.thonglam.service;

import com.thonglam.entity.Category;
import com.thonglam.exceptions.APIException;
import com.thonglam.exceptions.ResourceNotFoundException;
import com.thonglam.payload.CategoryDTO;
import com.thonglam.payload.CategoryResponse;
import com.thonglam.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder =  sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category>  categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty())
            throw new APIException("No category created till now ");
        List<CategoryDTO> categoryDtoList = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
       CategoryResponse response=new CategoryResponse();
       response.setContent(categoryDtoList);
       response.setPageNumber(categoryPage.getNumber());
       response.setPageSize(categoryPage.getSize());
       response.setTotalElements(categoryPage.getTotalElements());
       response.setTotalPages(categoryPage.getTotalPages());
       response.setLastPage(categoryPage.isLast());
        return response;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null)
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!!");
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Name", categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() ->
               new ResourceNotFoundException(String.valueOf(categoryDTO), String.valueOf(categoryDTO.getCategoryId()), categoryDTO.getCategoryName()));
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }
}
