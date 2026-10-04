package org.teach;

import java.util.*;

public class Main
{
    static Scanner input = new Scanner(System.in);
    static Random random = new Random();
    static List<String> levels = new ArrayList<String>();
    static List<Integer> score = new ArrayList<Integer>();
    static int easy = 0, medium = 0, hard = 0, sequence = 0;

    public static void main(String[] args)
    {
        mainMenu();
    }

    static int validAnswer()
    {
        while (!input.hasNextInt()) {
            System.out.println("Entrada inválida! Digite apenas números.\n");
            System.out.print("Sua Resposta: ");
            input.next();
        }
        return input.nextInt();
    }

    static int processHint(String text, int number, int points, int maxNumber, int lastGuess)
    {
        switch (text) {
            case "DP":
                points = Math.max(0, points - 10);
                System.out.println("DICA DE PARIDADE: O número é " + (number % 2 == 0 ? "PAR" : "ÍMPAR"));
                break;
            case "DI":
                points = Math.max(0, points - 20);
                int half = maxNumber / 2;
                if (number <= half) {
                    System.out.println("DICA DE INTERVALO: O número está na metade inferior (1 a " + half + ")");
                } else {
                    System.out.println("DICA DE INTERVALO: O número está na metade superior (" + (half + 1) + " a " + maxNumber + ")");
                }
                break;
            case "DPR":
                points = Math.max(0, points - 15);
                if (lastGuess == -1) {
                    System.out.println("DICA DE PROXIMIDADE: Você precisa fazer pelo menos um palpite numérico antes de usar esta dica!");
                } else {
                    int diff = Math.abs(lastGuess - number);
                    if (diff <= 5) {
                        System.out.println("DICA DE PROXIMIDADE: Muito Quente! (Diferença <= 5)");
                    } else if (diff <= 15) {
                        System.out.println("DICA DE PROXIMIDADE: Quente! (Diferença <= 15)");
                    } else {
                        System.out.println("DICA DE PROXIMIDADE: Frio!");
                    }
                }
                break;
            default:
                System.out.println("Comando de dica não reconhecido.");
                break;
        }
        return points;
    }

    static void createLog(String text, int points)
    {
        if (levels.size() == 10 && score.size() == 10) {
            levels.removeFirst();
            score.removeFirst();
        }
        levels.add(text);
        score.add(points);

        if (text.equals("Fácil") && easy < points) {
            easy = points;
        } else if (text.equals("Médio") && medium < points) {
            medium = points;
        } else if (text.equals("Difícil") && hard < points) {
            hard = points;
        } else if (text.equals("Sequência") && sequence < points) {
            sequence = points;
        }
    }

    static void mainMenu()
    {
        boolean running = true;

        do {
            System.out.println("""
                    |----------------------------------|
                    |====Bem-Vindo ao Desafio FINAL====|
                    |----------------------------------|
                    |       Escolha uma opção:         |
                    |                                  |
                    | Digite 1 para: INICIAR NOVO JOGO |
                    | Digite 2 para: VER REGRAS        |
                    | Digite 3 para: VER HISTÓRICO DE  |
                    |                PONTUAÇÕES        |
                    | Digite 4 para: SAIR              |
                    |                                  |
                    |----------------------------------|
                    """);
            System.out.print("Sua Resposta: ");
            int answer = validAnswer();

            switch (answer) {
                case 1: startGame(); break;
                case 2: rules(); break;
                case 3: pointsHistory(); break;
                case 4: running = false; break;
                default: System.out.println("Por favor escolha uma opção válida.\n");
            }
        } while (running);
    }

    static void startGame()
    {
        boolean inGame = true;

        do {
            System.out.println("""
                |----------------------------------|
                |==== Bem-Vindo a um Novo Jogo ====|
                |----------------------------------|
                |         Escolha um modo          |
                |             de Jogo:             |
                |                                  |
                | Digite 1 para: MODO FÁCIL        |
                | Digite 2 para: MODO MÉDIO        |
                | Digite 3 para: MODO DIFÍCIL      |
                | Digite 4 para: MODO DE SEQUÊNCIA |
                | Digite 5 para: VOLTAR AO MENU    |
                |                                  |
                |----------------------------------|
                """);

            System.out.print("Sua Resposta: ");
            int answer = validAnswer();

            switch (answer) {
                case 1: randomMode("Fácil", 10, 50, 100); break;
                case 2: randomMode("Médio", 7, 100, 200); break;
                case 3: randomMode("Difícil", 5, 200, 300); break;
                case 4: sequenceMode(); break;
                case 5: inGame = false; break;
                default: System.out.println("Por favor escolha uma opção válida.\n");
            }
        } while (inGame);
    }

    static void randomMode(String text, int gamble, int big, int score)
    {
        boolean playing = true;

        System.out.printf("""
                    ===================================
                    |      Bem-Vindo ao Modo %s     |
                    ===================================  
                    |                                 |
                    |    Você tem %d tentativas para  |
                    |   descobrir um número aleatório |
                    |           entre 1 e %d          |
                    |                                 |
                    | Caso você deseje receber uma    |
                    | dica:                           |
                    | Digite DP, para dica de paridade|
                    | Digite DI, para dica de         |
                    | intervalo                       |
                    | Digite DPR, para dica de        |
                    | proximidade                     |
                    |                                 |
                    ===================================
                    """, text, gamble, big);

        do
        {
            int chances = gamble;
            int points = score;
            int x = random.nextInt(1, big + 1);
            int lastGuess = -1;
            boolean guessed = false;

            while (chances > 0 && !guessed)
            {
                System.out.printf("\nTentativas restantes: %d | Pontuação Base/Atual: %d\n", chances, points);
                System.out.print("Sua Resposta (ou comando de dica): ");
                String inputStr = input.next().trim().toUpperCase();

                if (inputStr.equals("DP") || inputStr.equals("DI") || inputStr.equals("DPR")) {
                    points = processHint(inputStr, x, points, big, lastGuess);
                } else {
                    try {
                        int guess = Integer.parseInt(inputStr);
                        lastGuess = guess;

                        if (guess == x) {
                            guessed = true;
                            points += (chances - 1) * 50;
                            System.out.println("Parabéns, o número correto era " + x);
                        } else {
                            chances--;
                            System.out.println("Número incorreto!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida! Digite um número ou uma opção de dica válida (DP, DI, DPR).");
                    }
                }
            }

            if (!guessed)
            {
                System.out.println("\nInfelizmente você não conseguiu acertar, o número correto era " + x);
                points = 0;
            }

            createLog(text, points);

            System.out.println("===================================");
            String answer;
            do
            {
                System.out.println("\nDeseja jogar novamente neste modo? (SIM / NÃO): ");
                answer = input.next().toLowerCase();
            } while (!answer.equals("sim") && !answer.equals("não") && !answer.equals("nao"));

            playing = answer.equals("sim");
        } while (playing);
    }

    static void sequenceMode()
    {
        boolean playing = true;

        System.out.println("""
                ====================================
                |     MODO DE SEQUÊNCIA (BÔNUS)    |
                ====================================
                |      Adivinhe uma sequência      |
                |    de 3 números (entre 1 e 20)!  |
                ====================================
                """);
        do {
            int[] targetSeq = {random.nextInt(1, 21), random.nextInt(1, 21), random.nextInt(1, 21)};
            int chances = 10;
            int index = 0;

            while (chances > 0 && index < 3)
            {
                System.out.printf("\nTentativas restantes: %d | Adivinhando o %dº número\n", chances, index + 1);
                System.out.print("Sua Resposta: ");
                int guess = validAnswer();

                if (guess == targetSeq[index]) {
                    System.out.println("Acertou o " + (index + 1) + "º número!");
                    index++;
                } else {
                    chances--;
                    System.out.println("Incorreto para o " + (index + 1) + "º número!");
                }
            }

            int finalScore = 0;
            if (index == 3) {
                System.out.println("\nParabéns! Você descobriu toda a sequência!");
                finalScore = 200 + (chances * 50);
            } else {
                System.out.println("\nSuas tentativas acabaram! A sequência correta era: " + Arrays.toString(targetSeq));
            }

            createLog("Sequência", finalScore);
            String answer;
            do
            {
                System.out.println("\nDeseja jogar novamente neste modo? (SIM / NÃO): ");
                answer = input.next().toLowerCase();
            } while (!answer.equals("sim") && !answer.equals("não") && !answer.equals("nao"));

            playing = answer.equals("sim");
        }while(playing);

    }

    static void rules()
    {
        System.out.println("""
                =======================================================
                |                  REGRAS DO JOGO                     |
                =======================================================
                |1. MODO FÁCIL:                                       |
                |   - Intervalo: 1 a 50 | Tentativas: 10              |
                |     Base: 100 pts                                   |
                |                                                     |
                |2. MODO MÉDIO:                                       |
                |   - Intervalo: 1 a 100 | Tentativas: 7              | 
                |     Base: 200 pts                                   |
                |                                                     |
                |3. MODO DIFÍCIL:                                     |
                |   - Intervalo: 1 a 200 | Tentativas: 5              |
                |     Base: 300 pts                                   |
                |                                                     |
                |4. CÁLCULO DE PONTUAÇÃO:                             |
                |   - Pontuação Final = Base                          |
                |                       + (Tentativas Sobrando * 50)  |
                |                       - Penalidades                 |
                |                                                     |
                |5. SISTEMA DE DICAS (Custo em Pontos):               |
                |   - DP  (Paridade)     : -10 pontos                 |
                |   - DI  (Intervalo)    : -20 pontos                 |
                |   - DPR (Proximidade)  : -15 pontos                 |
                |                                                     |
                |6. MODO SEQUÊNCIA:                                   |
                |   - Descubra 3 números em 10 tentativas para vencer |
                |                                                     |
                =======================================================
                """);
    }

    static void pointsHistory()
    {
        boolean logs = true;

        do {
            System.out.println("""
                    ====================================
                    |====  Bem-Vindo ao Histórico  ====|
                    |====            de            ====|
                    |====        Pontuações        ====|
                    |----------------------------------|
                    |        Escolha uma opção         |
                    |         de Visualização:         |
                    |                                  |
                    | Digite 1 para: ÚLTIMOS DEZ JOGOS |
                    | Digite 2 para: RECORDE DE PONTOS |
                    | Digite 3 para: VOLTAR AO MENU    |
                    |                                  |
                    ====================================
                    """);

            System.out.print("Sua Resposta: ");
            int answer = validAnswer();

            switch (answer) {
                case 1: lastLogs(); break;
                case 2: bestScores(); break;
                case 3: logs = false; break;
                default: System.out.println("Por favor escolha uma opção válida.\n");
            }
        } while (logs);
    }

    static void lastLogs()
    {
        System.out.print("""
                =======================================
                |      MODO      |       PONTOS       |
                """);
        for (int i = 0; i < levels.size(); i++) {
            System.out.printf("""
                    =======================================
                    |     %-10s |         %-10d |
                    """, levels.get(i), score.get(i));
        }
        System.out.println("=======================================");
    }

    static void bestScores()
    {
        System.out.printf("""
                    =======================================
                    |         MELHORES PONTUAÇÕES         |
                    |                                     |
                    |      MODO      |       PONTOS       |
                    =======================================
                    |     Fácil      |         %-10d |
                    =======================================
                    |     Médio      |         %-10d |
                    =======================================
                    |     Difícil    |         %-10d |
                    =======================================
                    |    Sequência   |         %-10d |
                    =======================================
                    """, easy, medium, hard, sequence);
    }
}