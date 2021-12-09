package ssd.uz.wikiquickyapp.tuit;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tuit")
public class TuitTest {


    @PostMapping("/kk")
    public Long getNumber(@RequestBody TuitText tuitText){
        String text = tuitText.getName();
        String finderName = tuitText.getFinderName();

        int textLength = text.length();
        int finderNameLength = finderName.length();

        long count = 0;

        for (int i = 0; i < textLength-finderNameLength; i++) {
            String x1 = text.substring(i,i+finderNameLength);
            if (x1.equals(finderName)){
                count++;
            }
        }
        System.out.println(count);

        return count;

    }

    @PostMapping("/tarjima")
    public String findByName(@RequestBody TuitText tuitText){
        Suz suz1 = new Suz("kitob","book");
        Suz suz2 = new Suz("olma","apple");
        Suz suz3 = new Suz("tatu","tuit");
        Suz suz4 = new Suz("sanjar","sanjar");
        Suz suz5 = new Suz("maktab","school");

        String tarjima = "";

        List<Suz> suzList = new ArrayList<>();

        suzList.add(suz1);
        suzList.add(suz2);
        suzList.add(suz3);
        suzList.add(suz4);
        suzList.add(suz5);

        for (int i = 0; i < suzList.size(); i++) {

            if (suzList.get(i).getNameEn().equals(tuitText.getName())){
                tarjima=suzList.get(i).getNameUz();
                return tarjima;
            }else {
                tarjima=" bunaqa suz bazada mavjud emas";
            }
        }

        return tarjima;
    }

}
