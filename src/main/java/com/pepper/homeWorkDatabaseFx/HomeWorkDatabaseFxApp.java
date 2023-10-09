package com.pepper.homeWorkDatabaseFx;

import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeWorkDatabaseFxApp 
{
    @Autowired
    IncomeRepository incomeRepo;
    @Autowired
    PartnerRepository partnerRepo;
    
    /*Grafikus felülettel rendelkező alkalmazás (Swing vagy FX); a múltkori adatbázis használata (db__partners és db__income táblák)
    partner: id, név, tétel(ek, 
    
    csupán lekérdezések és az eredmények megjelenítése táblázatban, illetve valami összesítő/részletes nézetben (Label-ekkel)
    Váltás az alábbi funkciók között (gombokkal, menüvel, lenyíló listával; ahogy szeretnétek):

    -Legutóbbi 25 bevétel táblázatban (a partner neve, az összeg és a dátumok)
    -Kintlévőségek (nincs fizetve) táblázatban, illetve a kintlévőségek összege
    -Legutóbbi bevétel részletes nézetben (bevétel és partner minden adata látszik)
    -Partnerek listája (hozzájuk kapcsolódó projektek darabszámaival együtt)
    -------------
    
    
    */
   
    

    public static void main(String[] args) 
    {
        SpringApplication.run(HomeWorkDatabaseFxApp.class, args);
        Application.launch(HomeAppController.class, args);
        
        

    }

}
