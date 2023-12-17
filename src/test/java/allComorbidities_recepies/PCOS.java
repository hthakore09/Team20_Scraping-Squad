package allComorbidities_recepies;

import testBase.*;
import testBase.BaseClass;
import utilities.ConfigReader;
import utilities.WriteExcel;

//import com.tarladalal.recipe.scraping.utilities.WriteExcel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PCOS extends BaseClass{
	@Test
    public void extractRecipe() throws InterruptedException, IOException {
		ConfigReader.loadConfig();
		String strEliminators = ConfigReader.getEliminatorList();
		 String[] arrEliminators = strEliminators.split(","); 
		
		 List<String> PCOSEliminators = Arrays.asList(arrEliminators);
		 


        driver.findElement(By.xpath("//div/a[text()= 'Recipe A To Z']")).click();
        Thread.sleep(2000);
        WriteExcel writeOutput = new WriteExcel();
        // Create rows header
        writeOutput.setCellData("PCOS", 0, 0, "Recipe ID");
        writeOutput.setCellData("PCOS", 0, 1, "Recipe Name");
        writeOutput.setCellData("PCOS", 0, 2, "Recipe Category(Breakfast/lunch/snack/dinner)");
        writeOutput.setCellData("PCOS", 0, 3, "Food Category(Veg/non-veg/vegan/Jain)");
        writeOutput.setCellData("PCOS", 0, 4, "Ingredients");
        writeOutput.setCellData("PCOS", 0, 5, "Preparation Time");
        writeOutput.setCellData("PCOS", 0, 6, "Cooking Time");
        writeOutput.setCellData("PCOS", 0, 7, "Preparation method");
        writeOutput.setCellData("PCOS", 0, 8, "Nutrient values");
        writeOutput.setCellData("PCOS", 0, 9, "Targetted morbid conditions (Diabeties/Hypertension/Hypothyroidism)");
        writeOutput.setCellData("PCOS", 0, 10, "Recipe URL");
        
        
        int rowCounter = 1;
        // run in a loop for all recipe in a page
        List< String> pageBeginsWithList = Arrays.asList(new String[]{"0-9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"});
        for(int k=0; k < pageBeginsWithList.size(); k++) {
            driver.navigate().to("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith="+pageBeginsWithList.get(k));
            int lastPage =0;
            try {
                lastPage= Integer.parseInt(driver.findElement(By.xpath("//div/a[@class= 'respglink'][last()]")).getText());
                lastPage=2; // This needs to remove for all the pages 
            } catch ( Exception e) {
                //do nothing or log exception
            }
            if (0 != lastPage) {
                for (int j = 1; j <= lastPage; j++) {
                    int pageindex = j;
                    driver.navigate().to("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith="+pageBeginsWithList.get(k)+"&pageindex=" + j);
                    List<WebElement> recipeCardElements = driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));
                    List<String> recipeUrls = new ArrayList<>();
                    Map<String, String> recipeIdUrls = new HashMap<>();

                    //Looping through all recipes Web elements and generating a navigation URL
                    recipeCardElements.stream().forEach(recipeCardElement -> {
                        recipeUrls.add("https://www.tarladalal.com/" + recipeCardElement.findElement(By.xpath("//span[@class='rcc_recipename']/a")).getDomAttribute("href"));
                        //example: recipeIdUrls.put("id","url");=> Extracted Recipe Id and Recipe URL Here and added to a hashmap
                        recipeIdUrls.put(recipeCardElement.getDomAttribute("id").replace("rcp",""),"https://www.tarladalal.com/" + recipeCardElement.findElement(By.tagName("a")).getDomAttribute("href"));
                    });

                    for (Map.Entry<String,String> recipeIdUrlEntry : recipeIdUrls.entrySet())  {
                        String recipeUrl = recipeIdUrlEntry.getValue();
                        String recipeId = recipeIdUrlEntry.getKey();
                        driver.navigate().to(recipeUrl);
                        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

                        if (isEliminated(PCOSEliminators)) {
                            //driver.navigate().to("//div/a[text()= 'Recipe A To Z']");
                        } else {
                           
                            //Recipe id
                            try {
                                System.out.print(recipeId);
                                writeOutput.setCellData("PCOS", rowCounter, 0, recipeId);
                            } catch (Exception e) {

                            }

                            //Recipe Name
                            try {
                                WebElement recipeTitle = driver.findElement(By.xpath("//span[@id= 'ctl00_cntrightpanel_lblRecipeName']"));
                                System.out.print(recipeTitle.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 1, recipeTitle.getText());

                            } catch (Exception e) {

                            }
                            try {
                                WebElement recipeCategory = driver.findElement(By.xpath("//span[@itemprop= 'description']/*[contains (text(), 'breakfast') or contains (text(), 'lunch') or contains (text(), 'dinner')]"));
                                System.out.print(recipeCategory.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 2, recipeCategory.getText());

                            } catch (Exception e) {

                            }
                            try {
                                WebElement foodCategory = driver.findElement(By.xpath("//a/span[text()= 'No Cooking Veg Indian']"));
                                System.out.print(foodCategory.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 3, foodCategory.getText());

                            } catch (Exception e) {

                            }

                            try {
                                WebElement nameOfIngredients = driver.findElement(By.xpath("//div[@id= 'rcpinglist']"));
                                System.out.print(nameOfIngredients.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 4, nameOfIngredients.getText());

                            } catch (Exception e) {

                            }

                            try {
                                WebElement preparationTime = driver.findElement(By.xpath("//p/time[@itemprop= 'prepTime']"));
                                System.out.print(preparationTime.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 5, preparationTime.getText());

                            } catch (Exception e) {

                            }

                            try {
                                WebElement cookTime = driver.findElement(By.xpath("//p/time[@itemprop= 'cookTime']"));
                                System.out.print(cookTime.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 6, cookTime.getText());

                            } catch (Exception e) {

                            }

                            try {
                                WebElement prepMethod = driver.findElement(By.xpath("//div[@id= 'ctl00_cntrightpanel_pnlRcpMethod']"));
                                System.out.print(prepMethod.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 7, prepMethod.getText());

                            } catch (Exception e) {

                            }
                            try {
                                WebElement nutrients = driver.findElement(By.xpath("//table[@id= 'rcpnutrients']"));
                                System.out.print(nutrients.getText());
                                writeOutput.setCellData("PCOS", rowCounter, 8, nutrients.getText());

                            } catch (Exception e) {

                            	writeOutput.setCellData("PCOS", rowCounter, 9, "PCOS");
                            	
                            }
                            try {
                                System.out.print(recipeUrl);
                                writeOutput.setCellData("PCOS", rowCounter, 10, recipeUrl);
                            } catch (Exception e) {

                            }

                            rowCounter++;

                        }


                    }
                }
            }


        }


    }


    private boolean isEliminated(List<String> eliminators) {
        AtomicBoolean isEliminatorPresent = new AtomicBoolean(false);

        eliminators.parallelStream().forEach(eliminator -> {
            try {
                WebElement ingredientWebElement = driver.findElement(By.xpath("//div[@id= 'rcpinglist']"));
                String ingredients = ingredientWebElement.getText();
                if (null != ingredients && null != eliminator && ingredients.toLowerCase().contains(eliminator.toLowerCase())) {
                    isEliminatorPresent.set(true);
                }
            } catch (Exception e) {
                System.out.print("No Such Element " + e.getLocalizedMessage());
            }
            try {

                WebElement methodWebElement = driver.findElement(By.xpath("//div[@id='recipe_small_steps']"));
                String method = methodWebElement.getText();
                if (null != method && null != eliminator && method.toLowerCase().contains(eliminator.toLowerCase())) {
                    isEliminatorPresent.set(true);
                }
            } catch (Exception e) {
                System.out.print("No Such Element " + e.getLocalizedMessage());
            }
        });
        return isEliminatorPresent.get();
    }
}
