package merchant.numerals;

public abstract class GenericNumeral implements Comparable<GenericNumeral> {
    
    private String intergalacticName = null;
    private char romanName;
    private Integer value;
    
    public GenericNumeral(char romanName, int value)
    {
        this.romanName = romanName;
        this.value = value;
    }

    public abstract Integer combine(GenericNumeral gn);

    public abstract boolean isAvailable();

    public abstract void setAvailable(boolean available);

    public String getIntergalacticName(){ return this.intergalacticName; }

    public void setIntergalacticName(String intergalacticName){ this.intergalacticName = intergalacticName; }

    public char getRomanName(){ return this.romanName; }

    public Integer getValue(){ return this.value; }

    @Override
    public int compareTo(GenericNumeral n) {
        if (this.value > n.value)
            return 1;
        else if (this.value < n.value)
            return -1;
        return 0;
    }
    
    @Override
    public String toString()
    {
        return this.intergalacticName;    
    }
    
}