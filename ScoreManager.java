import java.util.ArrayList;
import java.util.List;
public class ScoreManager {
 private int score;
 private int lines;
 private int level;
 private List<IScoreObserver> observers;
 private static final int[] LINE_POINTS = {0, 100, 300, 500, 800};
 public ScoreManager() {
  this.score = 0;
  this.lines = 0;
  this.level = 1;
  this.observers = new ArrayList<>();
 }
 public void addObserver(IScoreObserver observer) {
  observers.add(observer);
 }
 public void removeObserver(IScoreObserver observer) {
  observers.remove(observer);
 }
 private void notifyScoreChanged() {
  for (IScoreObserver observer : observers) {
   observer.onScoreChanged(score);
  }
 }
 private void notifyLinesCleared(int linesCleared) {
  for (IScoreObserver observer : observers) {
   observer.onLinesCleared(linesCleared, score);
  }
 }
 public void addScore(int linesCleared) {
  if (linesCleared <= 0) return;
  int points = LINE_POINTS[Math.min(linesCleared, 4)] * level;
  score += points;
  lines += linesCleared;
  level = (lines / 10) + 1;
  notifyLinesCleared(linesCleared);
  notifyScoreChanged();
 }

 public void reset() {
  score = 0;
  lines = 0;
  level = 1;
  notifyScoreChanged();
 }
 public int getScore() {
  return score;
 }
 public int getLines() {
  return lines;
 }
 public int getLevel() {
  return level;
 }
 public int getFallSpeed() {
  return Math.max(100, 1000 - (level - 1) * 90);
 }
}