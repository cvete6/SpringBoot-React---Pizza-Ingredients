package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.exceptions.ExistingIngredientException;
import mk.finki.ukim.mk.lab.exceptions.SpicyIngredientsNumberExceededException;
import mk.finki.ukim.mk.lab.model.Ingredient;
import mk.finki.ukim.mk.lab.repository.jpa.IngredientsJpaRepository;
import mk.finki.ukim.mk.lab.service.IngredientService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PersistentIngredientServiceImpl implements IngredientService {

    private IngredientsJpaRepository ingredientsJpaRepository;

    public PersistentIngredientServiceImpl(IngredientsJpaRepository ingredientsJpaRepository) {

        this.ingredientsJpaRepository = ingredientsJpaRepository;

    }

    @Override
    public Ingredient addNewIngredient(String name, boolean spicy, float amount, boolean veggie) {

        if(spicy){
            int numSpicy=ingredientsJpaRepository.getSpicyCount();
            if(numSpicy==3) throw new SpicyIngredientsNumberExceededException();
        }
        Ingredient existingIngredient=ingredientsJpaRepository.findByName(name);
        if(existingIngredient!=null) throw new ExistingIngredientException();

        Ingredient newIngredient=new Ingredient(name,spicy,amount,veggie);
        return ingredientsJpaRepository.save(newIngredient);
    }

    @Override
    public Ingredient editIngredient(String id, boolean spicy, float amount, boolean veggie) {
        Ingredient ingredient= ingredientsJpaRepository.findByName(id);
        ingredient.setSpicy(spicy);
        ingredient.setAmount(amount);
        ingredient.setVeggie(veggie);
        ingredient.setName(id);
        ingredientsJpaRepository.save(ingredient);
        return ingredient;
    }

    @Override
    public void deleteIngredient(String id) {
        Ingredient ingredient= ingredientsJpaRepository.findByName(id);
        ingredientsJpaRepository.delete(ingredient);
    }

    @Override
    public Page<Ingredient> getAllIngredients(int page, int size) {

        Pageable pageData=PageRequest.of(page,size,Sort.by("name"));
        return ingredientsJpaRepository.findAll(pageData);

    }

    @Override
    public Ingredient getById(String id) {
        return ingredientsJpaRepository.findByName(id);
    }

    @Override
    public Page<Ingredient> getSpicy(int page, int size) {
        Pageable pageData=PageRequest.of(page,size);
        return ingredientsJpaRepository.getSpicy(pageData);
    }
}
