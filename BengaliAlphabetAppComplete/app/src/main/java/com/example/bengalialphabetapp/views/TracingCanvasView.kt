package com.example.bengalialphabetapp.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.bengalialphabetapp.model.Point
import com.example.bengalialphabetapp.model.StrokePath
import kotlin.math.abs

/**
 * Custom view that handles the tracing functionality for Bengali characters.
 * This view captures touch events, renders the user's strokes, and validates
 * them against the reference paths.
 */
class TracingCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Paint objects for different drawing needs
    private val strokePaint = Paint().apply {
        color = Color.parseColor("#2196F3") // Blue color for user strokes
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 12f
        isAntiAlias = true
    }

    private val fillPaint = Paint().apply {
        color = Color.parseColor("#8BC34A") // Light green for correct parts
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // Path for current user stroke
    private val currentPath = Path()
    
    // List to store all user stroke points for validation
    private val userStrokePoints = mutableListOf<Point>()
    
    // Reference stroke paths from the character being traced
    private var referenceStrokePaths: List<StrokePath> = emptyList()
    
    // Tracking variables for the current stroke
    private var currentStrokeIndex = 0
    private var isTracing = false
    private var lastX = 0f
    private var lastY = 0f
    
    // Listener for tracing events
    var tracingListener: TracingListener? = null
    
    // Tolerance for stroke validation (in pixels)
    private val strokeTolerance = 50f
    
    // Percentage of path that must be correct for validation
    private val validationThreshold = 0.7f
    
    /**
     * Set the reference stroke paths for the character being traced
     */
    fun setReferenceStrokePaths(paths: List<StrokePath>) {
        referenceStrokePaths = paths
        currentStrokeIndex = 0
        resetCanvas()
        invalidate()
    }
    
    /**
     * Reset the canvas for a new tracing attempt
     */
    fun resetCanvas() {
        currentPath.reset()
        userStrokePoints.clear()
        isTracing = false
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw the current user stroke
        canvas.drawPath(currentPath, strokePaint)
        
        // Draw filled areas for correctly traced parts
        // This would be implemented based on validation results
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }
    
    private fun touchStart(x: Float, y: Float) {
        currentPath.reset()
        currentPath.moveTo(x, y)
        lastX = x
        lastY = y
        userStrokePoints.clear()
        userStrokePoints.add(Point(x, y))
        isTracing = true
        
        tracingListener?.onStrokeStarted()
    }
    
    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - lastX)
        val dy = abs(y - lastY)
        
        // Only register movement if it's significant enough
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // Use quadratic bezier to smooth the line
            currentPath.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2)
            lastX = x
            lastY = y
            
            // Add point to the user stroke for validation
            userStrokePoints.add(Point(x, y))
            
            // Validate stroke in real-time
            validateCurrentStroke()
        }
    }
    
    private fun touchUp() {
        // Finalize the path
        currentPath.lineTo(lastX, lastY)
        
        // Complete validation of the stroke
        val isStrokeValid = validateStrokeCompletion()
        
        if (isStrokeValid) {
            tracingListener?.onStrokeCompleted(true)
            
            // Move to next stroke if available
            currentStrokeIndex++
            if (currentStrokeIndex >= referenceStrokePaths.size) {
                // All strokes completed successfully
                tracingListener?.onCharacterCompleted()
            }
        } else {
            tracingListener?.onStrokeCompleted(false)
        }
        
        // Reset for next stroke
        isTracing = false
        currentPath.reset()
    }
    
    /**
     * Validate the current stroke against the reference path in real-time
     */
    private fun validateCurrentStroke() {
        if (referenceStrokePaths.isEmpty() || currentStrokeIndex >= referenceStrokePaths.size) {
            return
        }
        
        // This would implement real-time validation logic
        // For now, we'll just provide feedback through the listener
        tracingListener?.onStrokeProgress(0.5f) // Placeholder value
    }
    
    /**
     * Validate the completed stroke against the reference path
     */
    private fun validateStrokeCompletion(): Boolean {
        if (referenceStrokePaths.isEmpty() || currentStrokeIndex >= referenceStrokePaths.size) {
            return false
        }
        
        // This would implement the full stroke validation logic
        // For now, we'll return a placeholder value
        return userStrokePoints.size >= 10 // Placeholder condition
    }
    
    /**
     * Interface for tracing event callbacks
     */
    interface TracingListener {
        fun onStrokeStarted()
        fun onStrokeProgress(progress: Float)
        fun onStrokeCompleted(isValid: Boolean)
        fun onCharacterCompleted()
    }
    
    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }
}
