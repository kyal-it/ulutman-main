package com.ulutman.model.entities;

import com.ulutman.model.enums.Category;

public interface SubCategory {
    Category getCategory(); // Возвращает категорию, к которой относится подкатегория
    String getName(); // В
}
