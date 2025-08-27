package org.apache.poi.sl.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.apache.poi.sl.usermodel.GroupShape;
import org.apache.poi.sl.usermodel.StrokeStyle;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TableShape;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.util.Internal;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/DrawTableShape.class */
public class DrawTableShape extends DrawShape {

    @Internal
    public static final int borderSize = 2;

    public DrawTableShape(TableShape<?, ?> shape) {
        super(shape);
    }

    protected Drawable getGroupShape(Graphics2D graphics) {
        if (this.shape instanceof GroupShape) {
            DrawFactory df = DrawFactory.getInstance(graphics);
            return df.getDrawable((GroupShape<?, ?>) this.shape);
        }
        return null;
    }

    @Override // org.apache.poi.sl.draw.DrawShape, org.apache.poi.sl.draw.Drawable
    public void applyTransform(Graphics2D graphics) {
        Drawable d = getGroupShape(graphics);
        if (d != null) {
            d.applyTransform(graphics);
        } else {
            super.applyTransform(graphics);
        }
    }

    @Override // org.apache.poi.sl.draw.DrawShape, org.apache.poi.sl.draw.Drawable
    public void draw(Graphics2D graphics) {
        Line2D.Double r39;
        Drawable d = getGroupShape(graphics);
        if (d != null) {
            d.draw(graphics);
            return;
        }
        TableShape<?, ?> ts = getShape();
        DrawPaint drawPaint = DrawFactory.getInstance(graphics).getPaint(ts);
        int rows = ts.getNumberOfRows();
        int cols = ts.getNumberOfColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TableCell<S, P> cell = ts.getCell(row, col);
                if (cell != 0 && !cell.isMerged()) {
                    Paint fillPaint = drawPaint.getPaint(graphics, cell.getFillStyle().getPaint());
                    graphics.setPaint(fillPaint);
                    Rectangle2D cellAnc = cell.getAnchor();
                    graphics.fill(cellAnc);
                    TableCell.BorderEdge[] arr$ = TableCell.BorderEdge.values();
                    for (TableCell.BorderEdge edge : arr$) {
                        StrokeStyle stroke = cell.getBorderStyle(edge);
                        if (stroke != null) {
                            graphics.setStroke(getStroke(stroke));
                            Paint linePaint = drawPaint.getPaint(graphics, stroke.getPaint());
                            graphics.setPaint(linePaint);
                            double x = cellAnc.getX();
                            double y = cellAnc.getY();
                            double w = cellAnc.getWidth();
                            double h = cellAnc.getHeight();
                            switch (edge) {
                                case bottom:
                                default:
                                    r39 = new Line2D.Double(x - 2.0d, y + h, x + w + 2.0d, y + h);
                                    break;
                                case left:
                                    r39 = new Line2D.Double(x, y, x, y + h + 2.0d);
                                    break;
                                case right:
                                    r39 = new Line2D.Double(x + w, y, x + w, y + h + 2.0d);
                                    break;
                                case top:
                                    r39 = new Line2D.Double(x - 2.0d, y, x + w + 2.0d, y);
                                    break;
                            }
                            graphics.draw(r39);
                        }
                    }
                }
            }
        }
        drawContent(graphics);
    }

    @Override // org.apache.poi.sl.draw.DrawShape, org.apache.poi.sl.draw.Drawable
    public void drawContent(Graphics2D graphics) {
        Drawable d = getGroupShape(graphics);
        if (d != null) {
            d.drawContent(graphics);
            return;
        }
        TableShape<?, ?> ts = getShape();
        DrawFactory df = DrawFactory.getInstance(graphics);
        int rows = ts.getNumberOfRows();
        int cols = ts.getNumberOfColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TextShape<?, ?> cell = ts.getCell(row, col);
                if (cell != null) {
                    DrawTextShape dts = df.getDrawable(cell);
                    dts.drawContent(graphics);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.poi.sl.draw.DrawShape
    public TableShape<?, ?> getShape() {
        return (TableShape) this.shape;
    }

    public void setAllBorders(Object... args) {
        TableShape<?, ?> table = getShape();
        int rows = table.getNumberOfRows();
        int cols = table.getNumberOfColumns();
        TableCell.BorderEdge[] edges = {TableCell.BorderEdge.top, TableCell.BorderEdge.left, null, null};
        int row = 0;
        while (row < rows) {
            int col = 0;
            while (col < cols) {
                edges[2] = col == cols - 1 ? TableCell.BorderEdge.right : null;
                edges[3] = row == rows - 1 ? TableCell.BorderEdge.bottom : null;
                setEdges(table.getCell(row, col), edges, args);
                col++;
            }
            row++;
        }
    }

    public void setOutsideBorders(Object... args) {
        if (args.length == 0) {
            return;
        }
        TableShape<?, ?> table = getShape();
        int rows = table.getNumberOfRows();
        int cols = table.getNumberOfColumns();
        TableCell.BorderEdge[] edges = new TableCell.BorderEdge[4];
        int row = 0;
        while (row < rows) {
            int col = 0;
            while (col < cols) {
                edges[0] = col == 0 ? TableCell.BorderEdge.left : null;
                edges[1] = col == cols - 1 ? TableCell.BorderEdge.right : null;
                edges[2] = row == 0 ? TableCell.BorderEdge.top : null;
                edges[3] = row == rows - 1 ? TableCell.BorderEdge.bottom : null;
                setEdges(table.getCell(row, col), edges, args);
                col++;
            }
            row++;
        }
    }

    public void setInsideBorders(Object... args) {
        if (args.length == 0) {
            return;
        }
        TableShape<?, ?> table = getShape();
        int rows = table.getNumberOfRows();
        int cols = table.getNumberOfColumns();
        TableCell.BorderEdge[] edges = new TableCell.BorderEdge[2];
        int row = 0;
        while (row < rows) {
            int col = 0;
            while (col < cols) {
                edges[0] = (col <= 0 || col >= cols - 1) ? null : TableCell.BorderEdge.right;
                edges[1] = (row <= 0 || row >= rows - 1) ? null : TableCell.BorderEdge.bottom;
                setEdges(table.getCell(row, col), edges, args);
                col++;
            }
            row++;
        }
    }

    private static void setEdges(TableCell<?, ?> cell, TableCell.BorderEdge[] edges, Object... args) {
        if (cell == null) {
            return;
        }
        for (TableCell.BorderEdge be : edges) {
            if (be != null) {
                if (args.length == 0) {
                    cell.removeBorder(be);
                } else {
                    for (Object o : args) {
                        if (o instanceof Double) {
                            cell.setBorderWidth(be, ((Double) o).doubleValue());
                        } else if (o instanceof Color) {
                            cell.setBorderColor(be, (Color) o);
                        } else if (o instanceof StrokeStyle.LineDash) {
                            cell.setBorderDash(be, (StrokeStyle.LineDash) o);
                        } else if (o instanceof StrokeStyle.LineCompound) {
                            cell.setBorderCompound(be, (StrokeStyle.LineCompound) o);
                        }
                    }
                }
            }
        }
    }
}
