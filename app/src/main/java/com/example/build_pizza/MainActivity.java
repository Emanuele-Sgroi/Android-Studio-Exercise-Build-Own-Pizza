package com.example.build_pizza;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private FrameLayout pizzaImageContainer;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout selectedIngredientsContainer, buttonsContainer;
    private TextView totalPriceText, selectionTitle;
    private Button buttonOrderBase, buttonClearSelection, buttonPlaceOrder;

    // Checkboxes for Ingredients
    private CheckBox checkSalami, checkMushrooms, checkRedPeppers, checkGreenPeppers, checkTomatoes, checkBlackOlives, checkBasil;

    // Prices
    private int basePrice = 6;
    private int totalPrice = 6;

    // Ingredient Prices and Mapping
    private final int SALAMI_PRICE = 2;
    private final int MUSHROOMS_PRICE = 1;
    private final int RED_PEPPERS_PRICE = 3;
    private final int GREEN_PEPPERS_PRICE = 2;
    private final int TOMATOES_PRICE = 1;
    private final int BLACK_OLIVES_PRICE = 4;

    // Ingredient Images
    private ImageView pizzaBaseImage;
    private final String[] ingredientNames = {"Salami", "Mushrooms", "Red Peppers", "Green Peppers", "Tomatoes", "Black Olives", "Basil"};
    private final int[] ingredientPrices = {SALAMI_PRICE, MUSHROOMS_PRICE, RED_PEPPERS_PRICE, GREEN_PEPPERS_PRICE, TOMATOES_PRICE, BLACK_OLIVES_PRICE, 0};
    private final int[] pizzaLayerImages = {
            R.drawable.pizza_salami,
            R.drawable.pizza_mashrooms,
            R.drawable.pizza_red_peppers,
            R.drawable.pizza_green_peppers,
            R.drawable.pizza_tomatoes,
            R.drawable.pizza_olives,
            R.drawable.pizza_basil
    };
    private final int[] ingredientIcons = {
            R.drawable.salami,
            R.drawable.mashroom,
            R.drawable.red_pepper,
            R.drawable.green_pepper,
            R.drawable.tomatoe,
            R.drawable.black_olives,
            R.drawable.basil
    };

    private ImageView[] pizzaLayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Elements
        pizzaImageContainer = findViewById(R.id.pizzaImageContainer);
        selectedIngredientsContainer = findViewById(R.id.selectedIngredientsContainer);
        buttonsContainer = findViewById(R.id.buttonsContainer);
        totalPriceText = findViewById(R.id.textTotalPrice);
        selectionTitle = findViewById(R.id.textSelectionTitle);
        buttonOrderBase = findViewById(R.id.buttonOrderBase);
        buttonClearSelection = findViewById(R.id.buttonClearSelection);
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);

        // Checkboxes
        checkSalami = findViewById(R.id.checkSalami);
        checkMushrooms = findViewById(R.id.checkMushrooms);
        checkRedPeppers = findViewById(R.id.checkRedPeppers);
        checkGreenPeppers = findViewById(R.id.checkGreenPeppers);
        checkTomatoes = findViewById(R.id.checkTomatoes);
        checkBlackOlives = findViewById(R.id.checkBlackOlives);
        checkBasil = findViewById(R.id.checkBasil);

        // Pizza base image
        pizzaBaseImage = findViewById(R.id.imagePizzaBase);

        // Initialize pizza layer images
        pizzaLayers = new ImageView[ingredientNames.length];
        for (int i = 0; i < pizzaLayers.length; i++) {
            pizzaLayers[i] = new ImageView(this);
            pizzaLayers[i].setImageResource(pizzaLayerImages[i]);
            pizzaLayers[i].setVisibility(View.GONE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    400,
                    400
            );
            params.gravity = android.view.Gravity.CENTER;
            pizzaLayers[i].setLayoutParams(params);
            pizzaImageContainer.addView(pizzaLayers[i]);
        }

        // Setup Checkboxes
        setupIngredientCheckboxes();

        // Button Actions
        buttonOrderBase.setOnClickListener(v -> showToast("You ordered just the pizza base. Total: £6"));

        buttonClearSelection.setOnClickListener(v -> clearSelection());

        buttonPlaceOrder.setOnClickListener(v -> showToast("Order placed! Total: £" + totalPrice));
    }

    private void setupIngredientCheckboxes() {
        CheckBox[] checkBoxes = {checkSalami, checkMushrooms, checkRedPeppers, checkGreenPeppers, checkTomatoes, checkBlackOlives, checkBasil};

        for (int i = 0; i < checkBoxes.length; i++) {
            int index = i;
            checkBoxes[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    addIngredient(index);
                } else {
                    removeIngredient(index);
                }
                updateButtons();
            });
        }
    }

    private void addIngredient(int index) {
        pizzaLayers[index].setVisibility(View.VISIBLE);
        totalPrice += ingredientPrices[index];
        addIngredientIcon(index);
        updateTotalPrice();
    }

    private void removeIngredient(int index) {
        pizzaLayers[index].setVisibility(View.GONE);
        totalPrice -= ingredientPrices[index];
        removeIngredientIcon(index);
        updateTotalPrice();
    }

    private void addIngredientIcon(int index) {
        ImageView ingredientIcon = new ImageView(this);
        ingredientIcon.setImageResource(ingredientIcons[index]);
        ingredientIcon.setTag(index);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                150, // width
                150  // height
        );
        params.setMargins(16, 8, 16, 8);
        ingredientIcon.setLayoutParams(params);

        selectedIngredientsContainer.addView(ingredientIcon);
        selectionTitle.setVisibility(View.VISIBLE);
        horizontalScrollView.setVisibility(View.VISIBLE);
    }


    private void removeIngredientIcon(int index) {
        for (int i = 0; i < selectedIngredientsContainer.getChildCount(); i++) {
            View child = selectedIngredientsContainer.getChildAt(i);
            if ((int) child.getTag() == index) {
                selectedIngredientsContainer.removeView(child);
                break;
            }
        }
        if (selectedIngredientsContainer.getChildCount() == 0) {
            selectionTitle.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);
        }
    }

    private void updateTotalPrice() {
        totalPriceText.setText("Total: £" + totalPrice);
    }

    private void updateButtons() {
        if (selectedIngredientsContainer.getChildCount() == 0) {
            buttonsContainer.setVisibility(View.GONE);
            buttonOrderBase.setVisibility(View.VISIBLE);
        } else {
            buttonsContainer.setVisibility(View.VISIBLE);
            buttonOrderBase.setVisibility(View.GONE);
        }
    }

    private void clearSelection() {
        // Temporarily remove the OnCheckedChangeListeners to avoid triggering removeIngredient calls
        CheckBox[] checkBoxes = {checkSalami, checkMushrooms, checkRedPeppers, checkGreenPeppers, checkTomatoes, checkBlackOlives, checkBasil};
        for (CheckBox cb : checkBoxes) {
            cb.setOnCheckedChangeListener(null);
            cb.setChecked(false);
        }

        // Hide all ingredient layers
        for (ImageView layer : pizzaLayers) {
            layer.setVisibility(View.GONE);
        }

        // Clear the selected ingredients container
        selectedIngredientsContainer.removeAllViews();
        selectionTitle.setVisibility(View.GONE);
        horizontalScrollView.setVisibility(View.GONE);

        // Reset the total price to the base price
        totalPrice = basePrice;
        updateTotalPrice();

        // Update buttons visibility
        updateButtons();

        // Re-attach the OnCheckedChangeListeners
        setupIngredientCheckboxes();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
