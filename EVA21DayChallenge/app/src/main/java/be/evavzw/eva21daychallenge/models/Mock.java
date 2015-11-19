package be.evavzw.eva21daychallenge.models;

/**
 * Created by Pieter-Jan on 28/10/2015.
 */
public class Mock {

    // RESTAURANTS
    /*Restaurant restaurant1 = new Restaurant()
    {
        RestaurantId = 1,
        Description = "Better than The Fat Duck.",
        Latitude = 51.0,
        Longitute = 44.0,
        Name = "The Vegan Duck",
        Website = "http://www.google.be"
    };
    Restaurant restaurant2 = new Restaurant()
    {
        RestaurantId = 2,
        Description = "Delicious dishes.",
        Latitude = 51.0,
        Longitute = 44.0,
        Name = "Quomodo",
        Website = "http://www.google.be"
    };
    Restaurant restaurant3 = new Restaurant()
    {
        RestaurantId = 3,
        Description = "This bar serves 100% vegetal beer.",
        Latitude = 51.0,
        Longitute = 44.0,
        Name = "The World's End",
        Website = "http://www.google.be"
    };
    Restaurant restaurant4 = new Restaurant()
    {
        RestaurantId = 4,
        Description = "We serve delicious fruity cocktails and soy milkshakes.",
        Latitude = 51.0,
        Longitute = 44.0,
        Name = "Raspberry",
        Website = "http://www.google.be"
    };
    Restaurant restaurant5 = new Restaurant()
    {
        RestaurantId = 5,
        Description = "All sorts of vegetal pie. Do not ask for meat pie.",
        Latitude = 51.0,
        Longitute = 44.0,
        Name = "Pi",
        Website = "http://www.google.be"
    };*/

    // INGREDIENTS
    Ingredient kikkererwt = new Ingredient(1, "Kikkererwt", "Blik", 1);
    Ingredient citroensap = new Ingredient(2, "Citroensap", "Eetlepel", 1);
    /*Ingredient knoflook = new Ingredient() {IngredientId = 3, Name = "Knoflook", Unit = "Teen"};
    Ingredient komijnpoeder = new Ingredient() {IngredientId = 4, Name = "Komijnpoeder", Unit = "Theelepel"};
    Ingredient tahin = new Ingredient() {IngredientId = 5, Name = "Tahin", Unit = "Eetlepel"};
    Ingredient olijfolie = new Ingredient() {IngredientId = 6, Name = "Olijfolie", Unit = "Eetlepel"};
    Ingredient zout = new Ingredient() { IngredientId = 7, Name = "Zout", Unit = "" };
    Ingredient peper = new Ingredient() {IngredientId = 8, Name = "Peper", Unit = ""};

    // COMPONENTS
    Component hummusKikkererwt = new Component() { Ingredient = kikkererwt, Quantity = 1.0 };
    Component hummusCitroensap = new Component() { Ingredient = citroensap, Quantity = 1.0 };
    Component hummusKnoflook = new Component() { Ingredient = knoflook, Quantity = 1.0 };
    Component hummusKomijnpoeder = new Component() { Ingredient = komijnpoeder, Quantity = 1.0 };
    Component hummusTahin = new Component() { Ingredient = tahin, Quantity = 2.0 };
    Component hummusOlijfolie = new Component() { Ingredient = olijfolie, Quantity = 3.0 };
    Component hummusZout = new Component() { Ingredient = zout, Quantity = 1.0 };
    Component hummusPeper = new Component() { Ingredient = peper, Quantity = 1.0 };

    // RECIPE PROPERTIES
    RecipeProperty hummusProperty1 = new RecipeProperty()
    {
        PropertyId = 1,
        Value = "Makkelijk",
        Type = "Moeilijkheid"
    };
    RecipeProperty hummusProperty2 = new RecipeProperty()
    {
        PropertyId = 2,
        Value = "15 min",
        Type = "Tijd"
    };
    RecipeProperty hummusProperty3 = new RecipeProperty()
    {
        PropertyId = 3,
        Value = "15 min",
        Type = "Aantal Personen"
    };

    // RECIPES
    Recipe hummus = new Recipe()
    {
        RecipeId = 1,
        Description = "Mix alles goed samen met een staafmixer en voeg eventueel nog wat extra water of olijfolie toe tot je een smeuig beleg krijgt.\nHummus is zeer lekker als broodbeleg.\nGebruik je gedroogde kikkererwten, zet dan 150 g kikkererwten een nachtje in de week en kook ze tot ze gaar zijn.",
        Image = "http://www.evavzw.be/sites/default/files/styles/wieni_gallery_photo/public/recipe/gallery/Hummus-02.jpg?itok=x49ueoqr",
        Ingredients = { hummusKikkererwt, hummusCitroensap, hummusKnoflook, hummusKomijnpoeder, hummusTahin, hummusOlijfolie, hummusZout, hummusPeper },
        Name = "Hummus",
        Properties = { hummusProperty1, hummusProperty2, hummusProperty3 }
    };

    // CHALLENGES
    Challenge cookingChallenge = new CreativeCookingChallenge()
    {
        ChallengeId = 1,
        Date = new DateTime(2015, 10, 12),
        Done = true,
        Ingredients = { kikkererwt },
        Earnings = 5,
        Name = "Kook met kikkererwten"
    };
    Challenge recipeChallenge = new RecipeChallenge()
    {
        ChallengeId = 2,
        Date = new DateTime(2015, 10, 13),
        Done = true,
        Recipe = hummus,
        Earnings = 5,
        Name = "Maak hummus"
    };*/

}
