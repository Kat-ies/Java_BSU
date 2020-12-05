package Excel;

import java.time.LocalDate;
import java.util.*;



public class Cell {

    private LocalDate date;

    private String formula;
    private boolean isWrongFormula;

    private String error;

    private Set<Cell> whereUsedCells;
    private Set<Cell> usedCells;

    Cell() {
        this.isWrongFormula = false;
        createSets();
    }

    Cell(LocalDate date, String formula) {
        this.date = date;
        this.formula = formula;
        createSets();
    }

    Cell(LocalDate date, String formula, boolean isWrongFormula, String error) {
        this.date = date;
        this.formula = formula;
        this.isWrongFormula = isWrongFormula;
        this.error = error;
        createSets();
    }

    private void createSets() {
        usedCells = new LinkedHashSet<>();
        whereUsedCells = new LinkedHashSet<>();
    }

    void assign(LocalDate date, String formula, boolean isFormulaWrong, String problem){
        this.date = date;
        this.formula = formula;
        this.isWrongFormula = isFormulaWrong;
        this.error = problem;
    }

    void assign(Cell other) {
        this.date = other.date;
        this.formula = other.formula;
        this.isWrongFormula = other.isWrongFormula;
        this.error = other.error;
    }

    String getError() {
        return error;
    }

    boolean isWrongFormula() {
        return isWrongFormula;
    }

    String getFormula() {
        return formula;
    }

    LocalDate getDate() {
        return date;
    }

    Set<Cell> getWhereUsedCells() {
        return whereUsedCells;
    }

    @Override
    public String toString() {
        if (date != null) {
            return date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear();
        } else {
            return formula;
        }
    }

    void addDependencies(List<Cell> cells){
        cells.forEach(this::addDependence);
    }

    void addDependence(Cell cell){
        this.usedCells.add(cell);
        cell.whereUsedCells.add(this);
    }

    void removeInUsedCells() {

        for (var usedCell : usedCells) {
            usedCell.whereUsedCells.remove(this);
        }
    }
}
