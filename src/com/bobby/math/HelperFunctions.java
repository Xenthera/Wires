package com.bobby.math;

import java.util.ArrayList;

public class HelperFunctions {

    public static double cubicBezierLine(double p1x, double p1y, double p2x, double p2y, double p3x,
                           double p3y, double p4x, double p4y, double a1x, double a1y, double a2x, double a2y) {
        double  ax, ay, bx, by, cx, cy, dx, dy;         // temporary variables
        double  c3x, c3y, c2x, c2y, c1x, c1y, c0x, c0y; // coefficients of cubic
        double  cl;         // c coefficient for normal form of line
        double  nx, ny;     // normal for normal form of line

        // used to determine if point is on line segment
        double  minx = Math.min(a1x, a2x),
                miny = Math.min(a1y, a2y),
                maxx = Math.max(a1x, a2x),
                maxy = Math.max(a1y, a2y);

        // Start with Bezier using Bernstein polynomials for weighting functions:
        //     (1-t^3)P1 + 3t(1-t)^2P2 + 3t^2(1-t)P3 + t^3P4
        //
        // Expand and collect terms to form linear combinations of original Bezier
        // controls.  This ends up with a vector cubic in t:
        //     (-P1+3P2-3P3+P4)t^3 + (3P1-6P2+3P3)t^2 + (-3P1+3P2)t + P1
        //             /\                  /\                /\       /\
        //             ||                  ||                ||       ||
        //             c3                  c2                c1       c0

        // Calculate the coefficients
        ax = p1x * -1; ay = p1y * -1;
        bx = p2x * 3;  by = p2y * 3;
        cx = p3x * -3; cy = p3y * -3;
        dx = ax + bx + cx + p4x;
        dy = ay + by + cy + p4y;
        c3x = dx; c3y = dy; // vec

        ax = p1x * 3;  ay = p1y * 3;
        bx = p2x * -6; by = p2y * -6;
        cx = p3x * 3;  cy = p3y * 3;
        dx = ax + bx + cx;
        dy = ay + by + cy;
        c2x = dx; c2y = dy; // vec

        ax = p1x * -3; ay = p1y * -3;
        bx = p2x * 3;  by = p2y * 3;
        cx = ax + bx;
        cy = ay + by;
        c1x = cx;
        c1y = cy; // vec

        c0x = p1x;
        c0y = p1y;

        // Convert line to normal form: ax + by + c = 0
        // Find normal to line: negative inverse of original line's slope
        nx = a1y - a2y;
        ny = a2x - a1x;

        // Determine new c coefficient
        cl = a1x * a2y - a2x * a1y;

        // ?Rotate each cubic coefficient using line for new coordinate system?
        // Find roots of rotated cubic
        var roots = getPolynomialRoots(new Double[]{nx * c3x + ny * c3y, nx * c2x + ny * c2y, nx * c1x + ny * c1y, nx * c0x + ny * c0y + cl});

        // Any roots in closed interval [0,1] are intersections on Bezier, but
        // might not be on the line segment.
        // Find intersections and calculate point coordinates
        for (var i = 0; i < roots.size(); i++) {
            var t = roots.get(i);

            if (0 <= t && t <= 1) { // We're within the Bezier curve
                // Find point on Bezier
                // lerp: x1 + (x2 - x1) * t
                var p5x = p1x + (p2x - p1x) * t;
                var p5y = p1y + (p2y - p1y) * t; // lerp(p1, p2, t);

                var p6x = p2x + (p3x - p2x) * t;
                var p6y = p2y + (p3y - p2y) * t;

                var p7x = p3x + (p4x - p3x) * t;
                var p7y = p3y + (p4y - p3y) * t;

                var p8x = p5x + (p6x - p5x) * t;
                var p8y = p5y + (p6y - p5y) * t;

                var p9x = p6x + (p7x - p6x) * t;
                var p9y = p6y + (p7y - p6y) * t;

                // candidate
                var p10x = p8x + (p9x - p8x) * t;
                var p10y = p8y + (p9y - p8y) * t;

                // See if point is on line segment
                if (a1x == a2x) {                       // vertical
                    if (miny <= p10y && p10y <= maxy) {
                        return 1;
                    }
                } else if (a1y == a2y) {               // horizontal
                    if (minx <= p10x && p10x <= maxx) {
                        return 1;
                    }
                } else if (p10x >= minx && p10y >= miny && p10x <= maxx && p10y <= maxy) {
                    return 1;
                }
            }
        }
        return 0;
    }

    final static double POLYNOMIAL_TOLERANCE = 1e-6;
    final static double TOLERANCE = 1e-12;

    static ArrayList<Double> getPolynomialRoots(Double[] args) {
        var C  = args;
        var degree  = C.length - 1;
        var n       = degree;
        var results = new ArrayList<Double>();
        for (var i = 0; i <= degree; i++) {
            if (Math.abs(C[i]) <= TOLERANCE) degree--; else break;
        }

        switch (degree) {
            case 1: getLinearRoots(C[n], C[n - 1], results); break;
            case 2: getQuadraticRoots(C[n], C[n - 1], C[n - 2], results); break;
            case 3: getCubicRoots(C[n], C[n - 1], C[n - 2], C[n - 3], results); break;
            default: break;
        }

        return results;
    }

    static ArrayList<Double> getLinearRoots(double C0, double C1, ArrayList<Double> results) {

        if (C1 != 0) results.add(-C0 / C1);
        return results;
    }

    static ArrayList<Double> getQuadraticRoots(double C0, double C1, double C2, ArrayList<Double> results) {

        double a = C2;
        double b = C1 / a;
        double c = C0 / a;
        double d = b * b - 4 * c;

        if (d > 0) {
            double e = Math.sqrt(d);

            results.add(0.5f * (-b + e));
            results.add(0.5f * (-b - e));
        } else if (d == 0) {
            results.add( 0.5f * -b);
        }

        return results;
    }


    static ArrayList<Double> getCubicRoots(double C0, double C1, double C2, double C3, ArrayList<Double> results) {

        double c3 = C3;
        double c2 = C2 / c3;
        double c1 = C1 / c3;
        double c0 = C0 / c3;

        double a       = (3 * c1 - c2 * c2) / 3;
        double b       = (2 * c2 * c2 * c2 - 9 * c1 * c2 + 27 * c0) / 27;
        double offset  = c2 / 3;
        double discrim = b * b / 4 + a * a * a / 27;
        double halfB   = b / 2;
        double tmp, root;

        if (Math.abs(discrim) <= POLYNOMIAL_TOLERANCE) discrim = 0;

        if (discrim > 0) {
            var e = Math.sqrt(discrim);

            tmp = -halfB + e;
            if ( tmp >= 0 ) root = Math.pow(  tmp, 1/3);
            else            root = -Math.pow(-tmp, 1/3);

            tmp = -halfB - e;
            if ( tmp >= 0 ) root += Math.pow( tmp, 1/3);
            else            root -= Math.pow(-tmp, 1/3);

            results.add(root - offset);
        } else if (discrim < 0) {
            var distance = Math.sqrt(-a/3);
            var angle    = Math.atan2(Math.sqrt(-discrim), -halfB) / 3;
            var cos      = Math.cos(angle);
            var sin      = Math.sin(angle);
            var sqrt3    = Math.sqrt(3);

            results.add( 2 * distance * cos - offset);
            results.add(-distance * (cos + sqrt3 * sin) - offset);
            results.add(-distance * (cos - sqrt3 * sin) - offset);
        } else {
            if (halfB >= 0) tmp = -Math.pow(halfB, 1/3);
            else            tmp =  Math.pow(-halfB, 1/3);

            results.add(2 * tmp - offset);
            // really should return next root twice, but we return only one
            results.add(-tmp - offset);
        }

        return results;
    }
}
