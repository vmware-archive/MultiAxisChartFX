/*
 * Copyright (c) 2014-2015 Pivotal Software, Inc.  All rights reserved.
 * 
 * Based on code by Oracle from javafx.scene.chart.XYChart.Data and modified
 * for multiple Y axes.
 * 
 * Licensed under the GNU General Public License version 2.0 subject to the
 * "Classpath" exception as provided by Pivotal in the LICENSE file that
 * accompanied this code.
 */
/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.pivotal.javafx.scene.chart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;


/**
 * A single data item with data for 2 axis charts
 */
public final class Data<X,Y> {
    // -------------- PUBLIC PROPERTIES ----------------------------------------

    boolean setToRemove = false;
    /** The series this data belongs to */
    private Series<X, Y> series;
    void setSeries(Series<X, Y> series) {
        this.series = series;
    }

    /** The generic data value to be plotted on the X axis */
    private ObjectProperty<X> xValue = new ObjectPropertyBase<X>() {
        @Override protected void invalidated() {
            // Note: calling get to make non-lazy, replace with change listener when available
            get();
            if (series!=null) {
                MultiAxisChart<X,Y> chart = series.getChart();
                if(chart!=null) chart.dataXValueChanged(Data.this);
            } else {
                // data has not been added to series yet :
                // so currentX and X should be the same
                setCurrentX(get());
            }
        }

        @Override
        public Object getBean() {
            return Data.this;
        }

        @Override
        public String getName() {
            return "XValue";
        }
    };
    /**
     * Gets the generic data value to be plotted on the X axis.
     * @return the generic data value to be plotted on the X axis.
     */
    public final X getXValue() { return xValue.get(); }
    /**
     * Sets the generic data value to be plotted on the X axis.
     * @param value the generic data value to be plotted on the X axis.
     */
    public final void setXValue(X value) {
        xValue.set(value);
        // handle the case where this is a init because the default constructor was used
        if (currentX.get() == null) currentX.setValue(value);
    }
    /** 
     * The generic data value to be plotted on the X axis.
     * @return The XValue property         
     */
    public final ObjectProperty<X> XValueProperty() { return xValue; }

    /** The generic data value to be plotted on the Y axis */
    private ObjectProperty<Y> yValue = new ObjectPropertyBase<Y>() {
        @Override protected void invalidated() {
            // Note: calling get to make non-lazy, replace with change listener when available
            get();
            if (series!=null) {
                MultiAxisChart<X,Y> chart = series.getChart();
                if(chart!=null) chart.dataYValueChanged(Data.this);
            } else {
                // data has not been added to series yet :
                // so currentY and Y should be the same
                setCurrentY(get());
            }
        }

        @Override
        public Object getBean() {
            return Data.this;
        }

        @Override
        public String getName() {
            return "YValue";
        }
    };
    /**
     * Gets the generic data value to be plotted on the Y axis.
     * @return the generic data value to be plotted on the Y axis.
     */
    public final Y getYValue() { return yValue.get(); }
    /**
     * Sets the generic data value to be plotted on the Y axis.
     * @param value the generic data value to be plotted on the Y axis.
     */
    public final void setYValue(Y value) {
        yValue.set(value);
        // handle the case where this is a init because the default constructor was used
        if (currentY.get() == null) currentY.setValue(value);
    }
    /** 
     * The generic data value to be plotted on the Y axis.
     * @return the YValue property
     */
    public final ObjectProperty<Y> YValueProperty() { return yValue; }

    /**
     * The generic data value to be plotted in any way the chart needs. For example used as the radius
     * for BubbleChart.
     */
    private ObjectProperty<Object> extraValue = new ObjectPropertyBase<Object>() {
        @Override protected void invalidated() {
            // Note: calling get to make non-lazy, replace with change listener when available
            get();
            if (series!=null) {
                MultiAxisChart<X,Y> chart = series.getChart();
                if(chart!=null) chart.dataExtraValueChanged(Data.this);
            }
        }

        @Override
        public Object getBean() {
            return Data.this;
        }

        @Override
        public String getName() {
            return "extraValue";
        }
    };
    public final Object getExtraValue() { return extraValue.get(); }
    public final void setExtraValue(Object value) { extraValue.set(value); }
    public final ObjectProperty<Object> extraValueProperty() { return extraValue; }

    /**
     * The node to display for this data item. You can either create your own node and set it on the data item
     * before you add the item to the chart. Otherwise the chart will create a node for you that has the default
     * representation for the chart type. This node will be set as soon as the data is added to the chart. You can
     * then get it to add mouse listeners etc. Charts will do their best to position and size the node
     * appropriately, for example on a Line or Scatter chart this node will be positioned centered on the data
     * values position. For a bar chart this is positioned and resized as the bar for this data item.
     */
    private ObjectProperty<Node> node = new SimpleObjectProperty<Node>(this, "node");
    public final Node getNode() { return node.get(); }
    public final void setNode(Node value) { node.set(value); }
    public final ObjectProperty<Node> nodeProperty() { return node; }

    /**
     * The current displayed data value plotted on the X axis. This may be the same as xValue or different. It is
     * used by MultiAxisChart to animate the xValue from the old value to the new value. This is what you should plot
     * in any custom MultiAxisChart implementations. Some MultiAxisChart chart implementations such as LineChart also use this
     * to animate when data is added or removed.
     */
    private ObjectProperty<X> currentX = new SimpleObjectProperty<X>(this, "currentX");
    final X getCurrentX() { return currentX.get(); }
    final void setCurrentX(X value) { currentX.set(value); }
    final ObjectProperty<X> currentXProperty() { return currentX; }

    /**
     * The current displayed data value plotted on the Y axis. This may be the same as yValue or different. It is
     * used by MultiAxisChart to animate the yValue from the old value to the new value. This is what you should plot
     * in any custom MultiAxisChart implementations. Some MultiAxisChart chart implementations such as LineChart also use this
     * to animate when data is added or removed.
     */
    private ObjectProperty<Y> currentY = new SimpleObjectProperty<Y>(this, "currentY");
    final Y getCurrentY() { return currentY.get(); }
    final void setCurrentY(Y value) { currentY.set(value); }
    final ObjectProperty<Y> currentYProperty() { return currentY; }

    /**
     * The current displayed data extra value. This may be the same as extraValue or different. It is
     * used by MultiAxisChart to animate the extraValue from the old value to the new value. This is what you should plot
     * in any custom MultiAxisChart implementations.
     */
    private ObjectProperty<Object> currentExtraValue = new SimpleObjectProperty<Object>(this, "currentExtraValue");
    final Object getCurrentExtraValue() { return currentExtraValue.getValue(); }
    final void setCurrentExtraValue(Object value) { currentExtraValue.setValue(value); }
    final ObjectProperty<Object> currentExtraValueProperty() { return currentExtraValue; }

    /**
     * Next pointer for the next data item. We maintain a linkedlist of the
     * data items so even after the data is deleted from the list,
     * we have a reference to it
     */
     protected Data<X,Y> next = null;

    // -------------- CONSTRUCTOR -------------------------------------------------

    /**
     * Creates an empty MultiAxisChart.Data object.
     */
    public Data() {}

    /**
     * Creates an instance of MultiAxisChart.Data object and initializes the X,Y
     * data values.
     * 
     * @param xValue The X axis data value
     * @param yValue The Y axis data value
     */
    public Data(X xValue, Y yValue) {
        setXValue(xValue);
        setYValue(yValue);
        setCurrentX(xValue);
        setCurrentY(yValue);
    }

    /**
     * Creates an instance of MultiAxisChart.Data object and initializes the X,Y
     * data values and extraValue.
     *
     * @param xValue The X axis data value.
     * @param yValue The Y axis data value.
     * @param extraValue Chart extra value.
     */
    public Data(X xValue, Y yValue, Object extraValue) {
        setXValue(xValue);
        setYValue(yValue);
        setExtraValue(extraValue);
        setCurrentX(xValue);
        setCurrentY(yValue);
        setCurrentExtraValue(extraValue);
    }

    // -------------- PUBLIC METHODS ----------------------------------------------

    /**
     * Returns a string representation of this {@code Data} object.
     * @return a string representation of this {@code Data} object.
     */ 
    @Override public String toString() {
        return "Data["+getXValue()+","+getYValue()+","+getExtraValue()+"]";
    }

}