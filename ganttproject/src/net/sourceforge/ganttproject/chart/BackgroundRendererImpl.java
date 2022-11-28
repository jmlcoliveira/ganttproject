/*
Copyright 2003-2012 Dmitry Barashev, GanttProject Team

This file is part of GanttProject, an opensource project management tool.

GanttProject is free software: you can redistribute it and/or modify 
it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

GanttProject is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.ganttproject.chart;

import java.awt.Color;

import biz.ganttproject.core.chart.canvas.Canvas;

public class BackgroundRendererImpl extends ChartRendererBase {

  public BackgroundRendererImpl() {

  }

  public BackgroundRendererImpl(ChartModel model) {
    super(model);
  }

  public Canvas paint() {
    return getPrimitiveContainer();
  }

  @Override
  public void render() {
    getPrimitiveContainer().clear();
    //Isto sao as dimensoes desse retangulo grande
    Canvas.Rectangle r = getPrimitiveContainer().createRectangle(0, 0, getWidth(), getHeight());
    Canvas.Text r1 = getPrimitiveContainer().createText(200,200,"Podre");
    //System.out.println("[Debug-009]: Isto muda a cor de fundo !!");
    r.setBackgroundColor(Color.CYAN);
    r1.setBackgroundColor(Color.RED);
  }
}
