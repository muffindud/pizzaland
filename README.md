# Pattern task:
Situation: You’ve met in the elevator the CEO of well known network of Pizzeria all over the world - PizzaLand. He started to tell you about his company. The biggest outlets are in the USA, Canada, Mexico and the UK.

The Pizzeria is well-known because of client-orientation because they adjust the recipes of their pizzas according to the country.

He said that his company misses some software for his wonderful pizzeria. The requirements are quite easy:

It should be a CLI application which provides the user with the following functionality:

* When user opens the app, it greets the user and shows him the menu. Menu has the following options:
  
  * Show menu
  
    * When user clicks to “Show menu” button, he’s provided with the list of possible pizzas.
    
    * The options in submenu:
      
      * Select pizza. When user selects pizza, application writes the description of it, price and recipe (ingredients). The recipe of the same pizza is different for each country. User can add it to the bag or return to the previous menu (list of pizzas)
      
      * Back (to the list of pizzas)
  
  * My Bag
    
    * When user opens this menu, he sees all the pizzas (qty/price) which user would like to buy. The user should see order total.
    
    * User can submit the order.
    
    * User can edit bag (edit number of pizzas/remove pizza) and clear it.
    
    * User can return to the previous menu (Main menu)
  
  * Set current country
    
    * Here user sees the country which is currently set. By default, it is the US. User should have possibility to change it.
    
    * User can return to the previous menu (Main menu)
  
  * Exit
    
    * Farewell the customer
    
    * Close the application

* Special requirements:

  * The pizza’s recipe is changed depending on the country. The list of pizza is always the same for all countries. Only recipe can be changed.
  
  * When user adds pizzas into the bag, once the order total is more or equal than 100\$, write down  “You’re about to spend 100\$. You won 5% discount” and apply this discount. Once user is in the bag, the total price is reduced by 5%.
  
  * When user submits the order, the order will be saved into a .json file with format order_random_uuid.json
  
You, as an engineer who has passed the design patterns course recently, propose your services!

**TIPS:**

1. For different pizzas creation use Factory method. In order to make same pizza for different country - use Abstract Factory. Depending on the factory, it will generate factory which generates pizza.

2. Pizza might have a lot of fields. Builder can be helpful.

3. In order to catch the moment when the total price is >=100, the Observer might be helpful.

4. It would be nice if you build the app according to the MVC/MVP/MVVM

5. For countries create additional enum. It will be a key parameter while building the abstract factory.

**Level of difficulty:**

1. Create basic models implementing patterns Factory method, Abstract Factory, Builder, Observer. Imitate user experience via hardcoded values.

2. Implement the CLI application with the menus and basic logic. No hardcode, only hardcore.

3. Implement validation of input, error handling mechanism. The system is stable and works even when the craziest QA is on duty. MVC/MVP/MVVM is implemented.

4. Static analysis of requirements. Revealing inaccuracies in requirements. Covering by basic unit tests (Mockito + JUnit/TestNG).
