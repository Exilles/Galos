package dc.galos.Controller;

import java.util.ArrayList;
import java.util.Random;

public class GameModes {

    private final int FROM_ENEMY_CIRCLES = 5;
    private final int TO_ENEMY_CIRCLES = 8;
    private final int MAX_FOOD_CIRCLES = 2;
    private final int INITIAL_ENEMY_CIRCLE_RADIUS = 40;
    private final int SMALL_ENEMY_CIRCLE_RADIUS = 20;

    private final int FROM_IMMORTAL_CIRCLES = 20;
    private final int TO_IMMORTAL_CIRCLES = 25;
    private final int MAX_IMMORTAL_CIRCLES = 5;

    private final int FROM_VANISHING_CIRCLES = 5;
    private final int TO_VANISHING_CIRCLES = 10;
    private final int MAX_VANISHING_CIRCLES = 5;

    private final int FOOD_CIRCLE_RADIUS_1 = 40;
    private final int FOOD_CIRCLE_RADIUS_2 = 70;

    private MainCircle mainCircle; // круг игрока
    private ArrayList<EnemyCircle> enemy_circles = new ArrayList<>(); // массив вражеских кругов
    private ArrayList<ImmortalCircle> immortal_circles = new ArrayList<>(); // массив неуязвимых кругов
    private ArrayList<VanishingCircle> vanishing_circles = new ArrayList<>(); // массив исчезающих кругов

    public GameModes(MainCircle mainCircle) {
        this.mainCircle = mainCircle;
    }

    public void generateMode(int mode){
        switch (mode){
            case 1: // Рандомная генерация кругов
                initEnemyCircles(1);
                initImmortalCircles(1);
                initVanishingCircles(2);
                break;
            case 2: // Поглощение кругов поочередно (с препятствием - неуязвимый круг)
                initEnemyCircles(2);
                initImmortalCircles(1);
                break;
            case 3: // Неуязвимые круги + 1 вражеский круг
                initEnemyCircles(3);
                initImmortalCircles(2);
                break;
            case 4: // Все исчезающие круги (с препятствием - неуязвимый круг)
                initImmortalCircles(1);
                initVanishingCircles(1);
                break;
        }
    }

    private void initEnemyCircles(int mode) {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        Random random = new Random();
        enemy_circles = new ArrayList<EnemyCircle>();
        int count_circles = FROM_ENEMY_CIRCLES + random.nextInt(TO_ENEMY_CIRCLES - FROM_ENEMY_CIRCLES);
        switch (mode){
            case 1:
                for (int i = 0; i < count_circles; i++) {
                    EnemyCircle circle;
                    if (count_circles == 2) {
                        do {
                            circle = EnemyCircle.getRandomCircle(FOOD_CIRCLE_RADIUS_2);
                        } while (circle.isIntersect(mainCircleArea));
                    }
                    else {
                        if (count_circles - i > MAX_FOOD_CIRCLES){
                            do {
                                circle = EnemyCircle.getRandomCircle();
                            } while (circle.isIntersect(mainCircleArea));
                        }
                        else {
                            do {
                                circle = EnemyCircle.getRandomCircle(FOOD_CIRCLE_RADIUS_1);
                            } while (circle.isIntersect(mainCircleArea));
                        }
                    }
                    enemy_circles.add(circle);
                }
                break;
            case 2:
                int radius = INITIAL_ENEMY_CIRCLE_RADIUS;
                count_circles = 5 + random.nextInt(2);
                for (int i = 0; i < count_circles; i++) {
                    EnemyCircle circle;
                    do {
                        circle = EnemyCircle.getRandomCircle(radius);
                    } while (circle.isIntersect(mainCircleArea));
                    enemy_circles.add(circle);
                    if (i < 4 ) radius += 20;
                    else radius += 45;
                }
                break;
            case 3:
                EnemyCircle circle;
                do {
                    circle = EnemyCircle.getRandomCircle(SMALL_ENEMY_CIRCLE_RADIUS);
                } while (circle.isIntersect2(mainCircleArea));
                enemy_circles.add(circle);
                break;
        }
        setEnemyCirclesColor();
    }

    private void initImmortalCircles(int mode) {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        Random random = new Random();
        immortal_circles = new ArrayList<ImmortalCircle>();
        int count_circles;
        switch (mode){
            case 1:
                count_circles = random.nextInt(MAX_IMMORTAL_CIRCLES);
                for (int i = 0; i < count_circles; i++) {
                    ImmortalCircle circle;
                    do {
                        circle = ImmortalCircle.getRandomCircle();
                    } while (circle.isIntersect(mainCircleArea));
                    immortal_circles.add(circle);
                }
                break;
            case 2:
                count_circles = FROM_IMMORTAL_CIRCLES + random.nextInt(TO_IMMORTAL_CIRCLES - FROM_IMMORTAL_CIRCLES);
                for (int i = 0; i < count_circles; i++) {
                    ImmortalCircle circle;
                    do {
                        circle = ImmortalCircle.getRandomCircle();
                    } while (circle.isIntersect(mainCircleArea));
                    immortal_circles.add(circle);
                }
                break;
        }
    }

    private void initVanishingCircles(int mode) {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        Random random = new Random();
        vanishing_circles = new ArrayList<VanishingCircle>();
        int count_circles;
        switch (mode){
            case 1:
                count_circles = FROM_VANISHING_CIRCLES + random.nextInt(TO_VANISHING_CIRCLES - FROM_VANISHING_CIRCLES);
                for (int i = 0; i < count_circles; i++) {
                    VanishingCircle circle;
                    if (count_circles == 2) {
                        do {
                            circle = VanishingCircle.getRandomCircle(FOOD_CIRCLE_RADIUS_2);
                        } while (circle.isIntersect(mainCircleArea));
                    }
                    else {
                        if (count_circles - i > MAX_FOOD_CIRCLES){
                            do {
                                circle = VanishingCircle.getRandomCircle();
                            } while (circle.isIntersect(mainCircleArea));
                        }
                        else {
                            do {
                                circle = VanishingCircle.getRandomCircle(FOOD_CIRCLE_RADIUS_1);
                            } while (circle.isIntersect(mainCircleArea));
                        }
                    }
                    vanishing_circles.add(circle);
                }
                break;
            case 2:
                count_circles = random.nextInt(MAX_VANISHING_CIRCLES);
                for (int i = 0; i < count_circles; i++) {
                    VanishingCircle circle;
                    do {
                        circle = VanishingCircle.getRandomCircle();
                    } while (circle.isIntersect(mainCircleArea));
                    vanishing_circles.add(circle);
                }
                break;
        }
        setVanishingCirclesColor();
    }

    private void setEnemyCirclesColor() {
        for (EnemyCircle circle : enemy_circles) {
            circle.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    private void setVanishingCirclesColor(){
        for (VanishingCircle circle : vanishing_circles) {
            circle.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    public ArrayList<EnemyCircle> getEnemy_circles() {
        return enemy_circles;
    }

    public ArrayList<ImmortalCircle> getImmortal_circles() {
        return immortal_circles;
    }

    public ArrayList<VanishingCircle> getVanishing_circles() {
        return vanishing_circles;
    }
}
