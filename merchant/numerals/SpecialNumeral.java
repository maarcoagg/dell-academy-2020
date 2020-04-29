package merchant.numerals;
/**
 *  O Número Especial é utilizado de forma mais restrita, pois 
 *  não podem ser subtraídos de nenhum símbolo.
 */
public class SpecialNumeral extends GenericNumeral {

    private boolean available = false; 

    public SpecialNumeral(char romanNumber, Integer value){
        super(romanNumber, value);
    }

    @Override
    public Integer combine(GenericNumeral gn) {
        if (super.compareTo(gn) < 0)
            throw new IllegalArgumentException(this.getIntergalacticName()+" can't be subtracted by any symbol.");
        else if (!this.isAvailable()){
            throw new IllegalArgumentException(this.getIntergalacticName()+" needs to be unique.");
        }        
        // se this >  gn, this não pode ser combinado pois é possível
        // que gn seja utilizado para subtrair outro número (Ex.: LIXIV)
        available = false;
        return super.getValue();
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }
}