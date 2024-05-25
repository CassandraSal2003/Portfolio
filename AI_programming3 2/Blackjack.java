//Cassandra Salazar
//Programming Assignment 3

import java.util.Random;

public class Blackjack {
    
    public static void main(String[] args) {
        prog3(3, 2, 6, 7, 100000, 100);
    }

    //Given a probability distribution p over the values from 1 to p.length, choose a random value. 
    //use pseudocode
    public static int chooseFromDist(double[] p) { 
        //store cumuilitive probabilities 
        double[] u = new double[p.length];
        u[0] = p[0]; 
        Random rand = new Random();
        double x = rand.nextDouble();

        //calculate probabilities 
        for (int k = 1; k < p.length; k++) {
            u[k] = u[k - 1] + p[k]; //cumulative sum probabilities up to specific index
        }
        //iterative thru cumulitive probabilities 
        for (int k = 0; k < p.length; k++) {
            if (x < u[k]) {
                return k + 1;
            }
        }

        return p.length; 
    }

    //Simulate the rolling of NDice dice with NSides sides
    public static int[] rollDice(int NDice, int NSides) {  
        Random rand = new Random();
        int[] rolls = new int[NDice];//store outcomes of die 
        for (int i = 0; i < NDice; i++) {
            rolls[i] = rand.nextInt(NSides) + 1;
        }
        return rolls;
    }

    //Pick the number of dice to roll, given the parameters, following the formulas in the assignment. You should work out what the numbers fk and pk are for some particular value of the above parameters, and then make sure that you are calculating them correctly.
    public static double[] chooseDice(int Score, int[][][] LoseCount, int[][][] WinCount, int NDice, double M) {
        int countDie = NDice;  
        int fk_index = 0;  
        double maxFk = 0;  
        double[] fkList = new double[countDie + 1];  
        double total = 0;  

        //calculate fk values 
        //find max fk
        for (int j = 0; j <= countDie; j++) {
            double denominator = WinCount[Score][NDice][j] + LoseCount[Score][NDice][j];
            double Fk = denominator > 0 ? WinCount[Score][NDice][j] / denominator : 0.5;

            fkList[j] = Fk;
            total += denominator;

            if (Fk > maxFk) {
                fk_index = j;
                maxFk = Fk;
            }
        }

        //calculate sum fk values (not max fk)
        double sum_fk = 0; 
        for (double fk : fkList) {
            sum_fk += fk;
        }
        sum_fk -= maxFk;

        //calculate probability of picking best number of dice to roll
        double bestChoiceProb = (total * maxFk + M) / (total * maxFk + countDie * M);
        double[] LISTdie_probability = new double[countDie + 1]; //store probabilities 

        //calculate probability for each number of dice 
        for (int j = 0; j <= countDie; j++) {
            if (j != fk_index) {
                double die_probability = (1 - bestChoiceProb) * (total * fkList[j] + M) / (sum_fk * total + (countDie - 1) * M);
                LISTdie_probability[j] = die_probability;
            } else {
                LISTdie_probability[j] = bestChoiceProb;
            }
        }
        return LISTdie_probability;
    }


    public static void PlayGame(int NDice, int NSides, int LTarget, int UTarget, int[][][] LoseCount, int[][][] WinCount, double M) {
        int score = LTarget; 
        int nDice = 1; 

        while (score <= UTarget) {
            int bestMove = chooseFromDist(chooseDice(score, LoseCount, WinCount, nDice, M));
            Random rand = new Random();
            boolean win = rand.nextDouble() < 0.5;

            //update WinCount or LoseCount based on outcome
            if (win) {
                WinCount[score][nDice][bestMove]++;
            } else {
                LoseCount[score][nDice][bestMove]++;
            }

            //update score and num of dice for next move
            score += bestMove;
            nDice++;
        }
    }

    //Given the final state of the arrays, extract the best move in each state and the probability of winning if you make that move.
    public static void extractAnswer(int[][][] winCount, int[][][] loseCount) {
        System.out.println("best move and probability of winning:\n");
        //iterate each state 
        for (int i = 0; i < winCount.length; i++) {
            for (int j = 0; j < winCount[i].length; j++) {
                for (int k = 0; k < winCount[i][j].length; k++) {
                    
                    //check if state was visited
                    if (winCount[i][j][k] + loseCount[i][j][k] > 0) {
                        //calculate probability of winning on current move 
                        double probWin = (double) winCount[i][j][k] / (winCount[i][j][k] + loseCount[i][j][k]);
                        System.out.printf("state: Score=%d, NumDice=%d, Move=%d, Probability=%.4f\n", i, j, k, probWin);
                    }
                }
            }
        }
    }    

    public static void prog3(int NDice, int NSides, int LTarget, int UTarget, int NGames, double M) {
        int maxNDice = NDice + 1;
        int[][][] loseCount = new int[UTarget + 1][maxNDice][maxNDice]; //store lose and win counts
        int[][][] winCount = new int[UTarget + 1][maxNDice][maxNDice];
        
        //update loseCount and winCount arrays
        //play game numerous times
        for (int game = 0; game < NGames; game++) {
            PlayGame(NDice, NSides, LTarget, UTarget, loseCount, winCount, M);
        }
        //initialize LoseCount and WinCount to 0
        for (int i = 0; i <= UTarget; i++) {
            for (int j = 0; j < maxNDice; j++) {
                for (int k = 0; k < maxNDice; k++) {
                    loseCount[i][j][k] = 0;
                    winCount[i][j][k] = 0;
                }
            }
        }
        //play again
        for (int game = 0; game < NGames; game++) {
            PlayGame(NDice, NSides, LTarget, UTarget, loseCount, winCount, M);
        }
    
        System.out.printf("Game: NDice=%d, NSides=%d, LTarget=%d, UTarget=%d\n\n", NDice, NSides, LTarget, UTarget);

        System.out.println("Exact solution:");
        //play matrix
        System.out.println("PLAY =");
        for (int i = 0; i < UTarget - LTarget + 1; i++) {
            for (int j = 0; j < NDice; j++) {
                System.out.print("\t" + (j + 1));
            }
            System.out.println();
        }
        System.out.println();
        
        //probability matrix
        System.out.println("PROB =");
        for (int i = 0; i < UTarget - LTarget + 1; i++) {
            for (int j = 0; j < NDice; j++) {
                double denom = winCount[i + LTarget][j + 1][j + 1] + loseCount[i + LTarget][j + 1][j + 1];
                double probWin = denom > 0 ? (double) winCount[i + LTarget][j + 1][j + 1] / denom : 0.5;
                System.out.printf("\t%.4f", probWin);
            }
            System.out.println();
        }
        
        //result ranges
        int start_i = LTarget == 0 ? 0 : 1;
        int start_j = 1;
        int end_i = NDice;
        int end_j = NDice;
    
        System.out.printf("\nThe meaningful results are [%d,%d] and [%d,%d] for I=%d..%d, J=%d..%d\n\n", 
                          start_i, start_j, end_i, end_j, start_i, end_i, start_j, end_j);
        
        //reinforcement learning experiment info
        System.out.println("Reinforcement learning experiment with M = " + M + ", NGames = " + NGames + "\n");
        extractAnswer(winCount, loseCount);
        //do multiple runs of game
        for (int run = 1; run <= 3; run++) {
            System.out.println("Run " + run + "\n");
            System.out.println("Play = ");
            for (int i = 0; i < NDice; i++) {
                for (int j = 0; j < NDice; j++) {
                    int val = i >= start_i && i <= end_i && j >= start_j && j <= end_j ? 2 : 0;
                    System.out.print("\t" + val);
                }
                System.out.println();
            }
            System.out.println("\nProb = ");
            for (int i = 0; i < NDice; i++) {
                for (int j = 0; j < NDice; j++) {
                    double prob = i >= start_i && i <= end_i && j >= start_j && j <= end_j ? 1.0 : 0.0;
                    System.out.printf("\t%.4f", prob);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    

}
