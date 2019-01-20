package a.dev.mobile_tolerance.forFragment;

class ToleranceItem {
    final String headerColumn;
    final String[] values;

    public ToleranceItem(String headerColumn, String[] values) {
        this.headerColumn = headerColumn;
        this.values = values;
    }

    @Override
    public String toString() {
        return headerColumn + " / " + values[0]+" / "+values[1];
    }
}

