import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();


    public static void main(String[] args) {

        creatingStreams("RLRFR", 100, 1000);

        LinkedHashMap<Integer, Integer> sortedMap = sort();

        giveTheFirstElementAndDeleteIt(sortedMap);

        listOfOtherSizes(sortedMap);
    }

    public static LinkedHashMap<Integer, Integer> sort(){
        return sizeToFreq.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));
    }


    public static void creatingStreams(String routes, int lengthWays, int countThreads){
        for (int i = 0; i < countThreads; i++) {
            new Thread(() -> {
                String text = generateRoute(routes, lengthWays);
                Map<Character, Integer> map = new HashMap<>();
                for (int k = 0; k < text.length(); k++) {
                    Integer n = map.get(text.charAt(k));
                    if (n == null) map.put(text.charAt(k), 1);
                    else map.put(text.charAt(k), n + 1);
                }

                synchronized (sizeToFreq) {
                    Integer characterChecker = map.get('R');//берем число повторений R допустим 76, это число становиться ключом для статичной мапы
                    Integer firstRepeat = 1;//это будет значение (количество повторений R в потоках здесь 1000 потоков), далее будем фиксировать количество повторяющихся
                    if (!sizeToFreq.containsKey(characterChecker)) {//если мапа не содержит количесво повторений (76) в потоке
                        sizeToFreq.put(characterChecker, firstRepeat);//ложим это количество повторений (ключ) в ячейку а в значении количество этих повторений,
                        // у нас на данный момент это 1
                    } else {
                        Integer repeatedRepetitions = sizeToFreq.get(characterChecker);//фиксируем количество повторений в переменную например 4
                        repeatedRepetitions = repeatedRepetitions + 1;//плюсуем 1 повторение
                        sizeToFreq.put(characterChecker, repeatedRepetitions);//изменяем количество повторений в мапе при сохраненном ключе
                    }
                }
            }).start();
        }
    }

    public static void giveTheFirstElementAndDeleteIt(LinkedHashMap<Integer, Integer> map) {
        int count = 1;
        for (Map.Entry<Integer, Integer> it : map.entrySet()) {
            if (count == 1) {
                System.out.println();
                System.out.println("Самое частое количество повторений  " + it.getKey() + " встретилось " + it.getValue() + " раз(а) \n");
                Integer key = it.getKey();
                map.remove(key);
                return;
            }
            count++;
        }
    }

    public static void listOfOtherSizes(LinkedHashMap<Integer, Integer> map) {
        System.out.println("Другие размеры: ");
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("- " + key + " (" + value + " раз(а))");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}

