package com.assignment.flamingo.base.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * @author Kate
 * @since 28-Apr-2026
 */
@StyleSheet("view-title.css")
public class ViewTitle extends Composite<HorizontalLayout> {

    public ViewTitle(String title) {
        addClassName("view-title");
        var h = new H1(title);
        getContent().add(new DrawerToggle(), h);
    }
}
