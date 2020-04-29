package merchant.numerals;

public class NormalNumeral extends GenericNumeral {

    private int count = 0;

    public NormalNumeral(char romanNumber, Integer value){
        super(romanNumber, value);
    }

    @Override
    public Integer combine(GenericNumeral gn) {
        int n1 = super.getValue(), n2 = gn.getValue();

        if (super.compareTo(gn) < 0)
        {
            if (n1*5 == n2 || n1*10 == n2 && gn.isAvailable())
            {
                count = 0;
                gn.setAvailable(false);
                return n2 - n1;
            }
            throw new IllegalArgumentException(this.getIntergalacticName()+" can't subtract "+gn.getIntergalacticName()+".");
        }
        else if (super.compareTo(gn) > 0)
        {
            this.setAvailable(true);
            return super.getValue();
        }
        else if (isAvailable())
        {
            count++;
            return super.getValue();
        }
        else throw new IllegalArgumentException("I can't repeat "+this.getIntergalacticName()+" more than 3 times.");
    }

    @Override
    public void setAvailable(boolean available) {
        count = 0;
    }

    @Override
    public boolean isAvailable() {
        if (count < 3 )
            return true;
        return false;
    }
}