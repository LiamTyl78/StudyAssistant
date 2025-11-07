package com.example;

import java.awt.Graphics;

import javax.swing.text.DefaultCaret;

public class InvisibleCaret extends DefaultCaret {
        public InvisibleCaret(){

        }
        @Override
        public void paint(Graphics g) {
            // Do nothing to prevent the caret from being drawn
        }

    }