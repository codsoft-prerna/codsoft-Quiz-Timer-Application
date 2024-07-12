import java.util.*;
import java.util.concurrent.*;

class QuizTimerApp {
    private String question;
    private List<String> options;
    int correctAnswerIndex;

    public QuizTimerApp(String question, List<String> options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isCorrect(int answer) {
        return answer == correctAnswerIndex;
    }
}

public class QuizApplication {
     int Timer = 10; // seconds
    private List<QuizTimerApp> questions;
    int score;

    public QuizApplication() {
        questions = new ArrayList<>();
        score = 0;
        loadQuestions();
    }

    private void loadQuestions() {
        questions.add(new QuizTimerApp("What is the capital of Japan?", 
                Arrays.asList("1. Seoul", "2. Tokyo", "3. Beijing", "4. Bangkok"), 1));
        questions.add(new QuizTimerApp("Which planet is known for its rings?", 
                Arrays.asList("1. Earth", "2. Mars", "3. Saturn", "4. Neptune"), 2));
        questions.add(new QuizTimerApp("What is the largest mammal in the world?", 
                Arrays.asList("1. Elephant", "2. Blue Whale", "3. Great White Shark", "4. Giraffe"), 1));
    }

    public void startQuiz() {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (QuizTimerApp question : questions) {
            System.out.println(question.getQuestion());
            for (String option : question.getOptions()) {
                System.out.println(option);
            }

            Future<Integer> future = executor.submit(() -> {
                return scanner.nextInt() - 1; // Convert to zero-based index
            });

            try {
                Integer answer = future.get(Timer, TimeUnit.SECONDS);
                if (question.isCorrect(answer)) {
                    System.out.println("Correct!");
                    score++;
                } else {
      System.out.println("Wrong! The correct answer was: " +  question.getOptions().get(question.correctAnswerIndex));
                                      
                }
            } catch (TimeoutException a) {
                System.out.println("Time's up! Moving to the next question.");
                future.cancel(true); // Cancel the input thread
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        
        executor.shutdown();
        
        System.out.println("Quiz finished! Your score: " + score + "/" + questions.size());
        
    }

    public static void main(String[] args) {
        QuizApplication quizApp = new QuizApplication();
        quizApp.startQuiz();
    }
}
