package merchant.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetalBook {

    private static class Metal implements Comparable<Metal>{
        Integer quantity, id;
        String name;
        Float credits;

        public Metal(Integer quantity, String name, Float credits){
            this.id = metalBook.size();
            this.quantity = quantity;
            this.name = name;
            this.credits = credits;
        }

        @Override
        public int compareTo(Metal other) {
            if (this.id > other.id)
                return 1;
            else if (this.id < other.id)
                return -1;
            else return 0;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(". ").
               append(quantity).append(" of ").
               append(name).append(" costs ").
               append(credits).append(" credits.");
            return sb.toString();
        }
    }

    private static Map<String, Metal> metalBook = new HashMap<>();

    public MetalBook(){}

    public boolean addMetal(Integer quantity, String name, Float credits)
    {
        if (!metalBook.containsKey(name))
        {
            metalBook.put(name, new Metal(quantity, name, credits));
            return true;
        } return false;
    }

    public Float calculatePrice(String metal, Integer quantity)
    {
        if (metalBook.containsKey(metal)){
            Metal m = metalBook.get(metal);
            Float price = (m.credits / m.quantity) * quantity;
            return price;
        } else throw new IllegalArgumentException("The metal "+ metal +"is not cataloged.");
    }

    public boolean existsMetal(String metal) { return metalBook.containsKey(metal); }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        List<Metal> metalList = new ArrayList<>();

        for(Metal metal : metalBook.values())
            metalList.add(metal);
        
        Collections.sort(metalList);
        metalList.forEach(m -> sb.append(m.toString()).append("\n"));
        return sb.toString();
    }
}