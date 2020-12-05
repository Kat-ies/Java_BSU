package Excel;

import Excel.Exceptions.CycleException;

import java.time.LocalDate;
import java.time.format.*;
import java.util.*;



class Controller {

    void setValue(ExcelTableModel model, String value, int rowIndex, int columnIndex) {

        var table = model.getData();
        var cell = table[rowIndex][columnIndex];

        cell.removeInUsedCells();

        Cell newCell;

        if (value.isEmpty()) {
            newCell = new Cell(null, null, false, null);
        } else {
            newCell = changeCell(table, cell, value);
        }

        cell.assign(newCell);

        if (!newCell.isWrongFormula()) {
            try {
                updateCell(table, cell, cell);
            } catch (CycleException e) {
                cell.assign(null, value, true, e.getMessage());
            }
            model.fireTableDataChanged();
        }
    }

    private Cell changeCell(Cell[][] table, Cell cell, String value) {

        try {
            if (matchesFormat(value)) {
                return makeOperation(value, table, cell);
            } else {
                return new Cell(null, value, true, "#invalid formula");
            }

        } catch (DateTimeParseException e) {
            return new Cell(null, value, true, "#invalid date");

        }catch (NullPointerException e) {
            return new Cell(null, value, true, "#reference to empty cell");
        }
    }

    private void updateCell(Cell[][] table, Cell targetCell, Cell parentCell) throws CycleException {

        for (var cell : targetCell.getWhereUsedCells()) {

            if (cell == parentCell) {
                throw new CycleException("#cycle error");
            }

            var ans = changeCell(table, cell, cell.getFormula());

            cell.assign(ans);
            updateCell(table, cell, parentCell);
        }
    }

    private boolean matchesFormat(String str) {

        var spaces = "\\s*";
        var date = "(\\d{1,2}[.]\\d{1,2}[.]\\d{4})";
        var ref = "([A-Z](([0-9])|([1][0-9])|([2][0])))";
        var dateOrRef = "(" + date + "|" + ref + ")";
        var minOrMax = spaces + "([M]([I][N]|[A][X]))" + spaces;

        return str.matches("\\s*(([=]\\s*(" + dateOrRef + "\\s*[+-]\\s*\\d+))|" + date +")\\s*")
                || str.matches("\\s*[=]" + minOrMax + "[(]\\s*" + dateOrRef + "((\\s*[,]\\s*" + dateOrRef + ")*)\\s*[)]\\s*");
    }

    private Cell makeOperation(String str, Cell[][] table, Cell cell)
            throws DateTimeParseException {

        var parsedString = parseString(str);
        var formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate localDate;

        if (parsedString.get(0).equals("=")) {

            if (parsedString.get(1).equals("MIN")) {
                var dates = convertStringsToDates(parsedString, table, cell);
                var date = Collections.min(dates);
                return new Cell(date, str);

            } else if (parsedString.get(1).equals("MAX")) {
                var dates = convertStringsToDates(parsedString, table, cell);
                var date = Collections.max(dates);
                return new Cell(date, str);

            } else if (parsedString.get(1).matches("(\\d{1,2}[.]\\d{1,2}[.]\\d{4})")) {
                localDate = LocalDate.parse(parsedString.get(1), formatter);

            } else {
                var columnIndex = parsedString.get(1).charAt(0) + 1 - 'A';
                var rowIndex = Integer.parseInt(parsedString.get(1).substring(1)) - 1;
                var tableRef = table[rowIndex][columnIndex];
                localDate = tableRef.getDate();
                cell.addDependence(tableRef);
            }

            if (parsedString.get(2).equals("+")) {
                var daysNumber = Integer.parseInt(parsedString.get(3));
                var date = localDate.plusDays(daysNumber);
                return new Cell(date, str);

            } else {
                var daysNumber = Integer.parseInt(parsedString.get(3));
                var date = localDate.minusDays(daysNumber);
                return new Cell(date, str);
            }

        } else {
            var date = LocalDate.parse(parsedString.get(0), formatter);
            return new Cell(date, str);
        }
    }

    private List<String> parseString(String str) {

        var tokenizer = new StringTokenizer(str, "=+- (),", true);
        var list = new ArrayList<String>();

        while (tokenizer.hasMoreTokens()) {

            var token = tokenizer.nextToken();

            if (!(token.equals(" ") || token.equals(",") || token.equals(")") || token.equals("("))){
                list.add(token);
            }
        }

        return list;
    }

    private List<LocalDate> convertStringsToDates(List<String> parsedString,
                                                  Cell[][] table,
                                                  Cell cell)
            throws DateTimeParseException {

        var formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        var datesStrings = parsedString.subList(2, parsedString.size());
        var dates = new LinkedList<LocalDate>();
        var cells = new LinkedList<Cell>();

        for (var dateString : datesStrings){

            if (dateString.matches("(\\d{1,2}[.]\\d{1,2}[.]\\d{4})")){
                dates.add(LocalDate.parse(dateString, formatter));

            } else {
                int column = (dateString.charAt(0) + 1 - 'A');
                int row = Integer.parseInt(dateString.substring(1)) - 1;
                dates.add(table[row][column].getDate());
                cells.add(table[row][column]);
            }
        }

        cell.addDependencies(cells);

        return dates;
    }
}
