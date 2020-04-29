package merchant.tools;

import merchant.numerals.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Translator {

    private static Map<String, GenericNumeral> nums;
    private static MetalBook mb = new MetalBook();
    
    public Translator() {
        nums = new HashMap<String, GenericNumeral>(7);
        nums.put("I",new NormalNumeral('I', 1));
        nums.put("V",new SpecialNumeral('V', 5));
        nums.put("X",new NormalNumeral('X', 10));
        nums.put("L",new SpecialNumeral('L', 50));
        nums.put("C",new NormalNumeral('C', 100));
        nums.put("D",new SpecialNumeral('D', 500));
        nums.put("M",new NormalNumeral('M', 1000));
    }

    public void transactionReader(File f)
    {
        // Leitura da transacao é dividida por contextos
        boolean isTranslating = true, storingPrices = false, askingQuestions = false;
        try {
            String[] words;
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine())
            {
                // Pega linha de texto
                String line = sc.nextLine();
                words = line.split(" ");

                // Se sintaxe for de indicação de notações intergalácticas
                if(words.length == 3 && words[1].equals("is"))
                {
                    int index = 0;
                    // Verifica se está no contexto correto
                    if (isTranslating && index < 7)
                    {
                        do
                        {
                            // Traduz numeral
                            char romanName = words[words.length-1].charAt(0);
                            if(!setIntergalacticNumeral(romanName,words[0]))
                                System.err.println("SINTAX ERROR: I have no idea what is: "+romanName+".");

                            // Atualiza para próximo linha
                            if (sc.hasNextLine()){
                                line = sc.nextLine();
                                words = line.split(" ");
                                index++;
                                /*  Se a próxima linha não é uma tradução, 
                                 *  altera o contexto para storingPrices    */
                                if (words.length != 3 || index > 7)
                                {
                                    isTranslating = false;
                                    storingPrices = true;
                                }
                            }                        
                        } while(isTranslating && index < 7 && words.length == 3 && words[1].equals("is"));
                    } else System.err.println("Out of context intergalactic notation.");
                }

                // Se sintaxe for de indicação de valores em créditos
                if (isStoragePriceSintax(words))
                {
                    if (storingPrices)
                    {
                        do
                        {
                            if(!storeMetal(words))
                                System.err.println("SINTAX ERROR: Can't store metal.");
                            
                            // Atualiza para próximo linha
                            if (sc.hasNextLine())
                            {
                                line = sc.nextLine();
                                words = line.split(" ");
                                /*  Se a próxima linha não é uma precificação, 
                                 *  altera o contexto para askingQuestions    */
                                if (!isStoragePriceSintax(words))
                                {
                                    storingPrices = false;
                                    askingQuestions = true;
                                }
                            } 
                        }while(isStoragePriceSintax(words));
                    } else System.err.println("Out of context price storage.");
                } 
                else 
                {
                    storingPrices = false;
                    askingQuestions = true;
                }

                if (isAskingQuestionSintax(words))
                {
                    if (askingQuestions)
                    {
                        do
                        {
                            System.out.println(askQuestion(words));

                            // Atualiza para próximo linha
                            if (sc.hasNextLine())
                            {
                                line = sc.nextLine();
                                words = line.split(" ");
                            } else askingQuestions = false;
                        }while(askingQuestions);
                    } else System.err.println("Out of context question.");
                } 
            }
            sc.close();
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    private boolean setIntergalacticNumeral(char rn, String gn){
        String romanName = rn+"";

        if (nums.containsKey(romanName)){
            GenericNumeral aux = nums.remove(romanName);
            aux.setIntergalacticName(gn);
            nums.put(gn,aux);
            return true;
        }
        return false;        
    }

    private boolean storeMetal(String[] words)
    {
        Integer quantity = 0, i = 0;
        String name = null;
        Float credits = 0f;
        List<GenericNumeral> intergalacticValues = new ArrayList<>();
        // Pega todos numerais intergaláticos em sucessão
        for(i = 0; i < words.length; i++)
        {
            if (nums.containsKey(words[i])){
                intergalacticValues.add(nums.get(words[i]));
            } else break;
        }
        // Tenta fazer as conversões necessárias e armazenar o metal
        try
        {
            quantity = parseIntergalacticValues(intergalacticValues); // Converte para numerais arábicos
            credits = Float.parseFloat(words[words.length-2]);
            if (words[i+1].equalsIgnoreCase("is"))
                name = words[i];
            else throw new IllegalArgumentException("A metal cannot have a composite name.");
            // Armazena atributos do metal
            mb.addMetal(quantity, name, credits);

            return true;                                
        } catch(NumberFormatException e) {
            System.err.println(e);
        } catch(IllegalArgumentException e) {
            System.err.println(e);
        }
        return false;    
    }

    private Integer parseIntergalacticValues(List<GenericNumeral> intergalacticValues)
    {
        if (!intergalacticValues.isEmpty())
        {
            Integer convertedValue = 0;
            for(int i = 0; i < intergalacticValues.size(); i++)
            {
                GenericNumeral num = intergalacticValues.get(i);
                if (num.isAvailable())
                {
                    if (i+1 < intergalacticValues.size()) {
                        try {
                            convertedValue += num.combine(intergalacticValues.get(i+1));
                        } catch (IllegalArgumentException e) {
                            System.err.println(e+" Values: "+intergalacticValues.toString());
                            return null;
                        }
                    }
                    else convertedValue += num.getValue();
                }
            }
            return convertedValue;
        }
        throw new IllegalArgumentException("Can't convert an empty value.");
    }

    private boolean isStoragePriceSintax(String[] words)
    {
        if (words != null && 
            words.length >= 5 &&
            words[words.length-1].equalsIgnoreCase("credits") && 
            words[words.length-3].equalsIgnoreCase("is"))
            return true;
        return false;
    }

    private boolean isAskingQuestionSintax(String[] words)
    {
        // Verifica estrutura básica da pergunta
        if (words != null && words.length >= 4 && words[words.length-1].equals("?"))
        {
            for(int i = 0; i < words.length; i++)
                words[i] = words[i].toLowerCase();
            // Verifica se a pergunta em si é valida
            if ( (words[0].equals("quanto") && words[1].equals("vale")) || words[0].equals("quantos") && words[1].equals("créditos") && words[2].equals("são"))
                return true;
        }
        return false;            
    }

    private String askQuestion(String[] words)
    {
        List<GenericNumeral> intergalacticValues = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String questionType = words[0].toLowerCase(), answer = null;
        switch(questionType)
        {
            case "quanto":
            Integer numeral = 0;
            for (int i = 2; i < words.length; i++)
            {
                if (nums.containsKey(words[i]))
                {
                    GenericNumeral value = nums.get(words[i]);
                    intergalacticValues.add(value);
                    sb.append(value).append(" ");
                }
                else if (words[i].equals("?"))
                {
                    numeral = parseIntergalacticValues(intergalacticValues);
                    sb.append("is ").append(numeral);
                    answer = sb.toString();
                    break;
                }
                else 
                {
                    answer = "I have no idea what you are talking about.";
                    break;
                }
            }
            break;
            case "quantos":
            DecimalFormat df = new DecimalFormat("##.##");
            for (int i = 3; i < words.length; i++)
            {
                if (nums.containsKey(words[i]))
                {
                    GenericNumeral value = nums.get(words[i]);
                    intergalacticValues.add(value);
                    sb.append(value).append(" ");
                }
                else if (mb.existsMetal(words[i]))
                {
                    Integer quantity = parseIntergalacticValues(intergalacticValues);
                    Float credits = mb.calculatePrice(words[i], quantity);
                    sb.append(words[i]).append(" is ").append(df.format(credits)).append(" Credits.");
                    answer = sb.toString();
                    break;
                }
                else 
                {
                    answer = "I have no idea what you are talking about.";
                    break;
                }
            }
        }
        return answer;
    }

    public void printLine(String[] words)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < words.length; i++)
            sb.append(words[i]).append(" ");
        System.out.println(sb.toString());
    }

    @Override
    public String toString(){
        return nums.toString();
    }
}