package com.example.bengalialphabetapp.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.bengalialphabetapp.model.Point
import com.example.bengalialphabetapp.model.StrokePath

/**
 * Custom view that displays guidance overlays for Bengali character tracing.
 * This view shows dotted paths, directional arrows, and other visual cues
 * to help users trace characters correctly after failed attempts.
 */
class GuidanceOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Paint objects for different guidance elements
    private val dotPaint = Paint().apply {
        color = Color.parseColor("#FFEB3B") // Yellow for guidance dots
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val arrowPaint = Paint().apply {
        color = Color.parseColor("#FF9800") // Orange for directional arrows
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 8f
        isAntiAlias = true
    }

    private val pathPaint = Paint().apply {
        color = Color.parseColor("#FFEB3B") // Yellow for path lines
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 6f
        isAntiAlias = true
        // Set dashed effect for dotted path
        setPathEffect(android.graphics.DashPathEffect(floatArrayOf(10f, 20f), 0f))
    }

    // Current stroke path being guided
    private var currentStrokePath: StrokePath? = null
    
    // Path for drawing the guidance
    private val guidancePath = Path()
    
    // Animation properties
    private var animationProgress = 0f
    private val animationDuration = 2000L // 2 seconds for full animation
    private var lastAnimationTime = 0L
    private var isAnimating = false
    
    /**
     * Set the stroke path for which guidance should be shown
     */
    fun setGuidanceForStroke(strokePath: StrokePath) {
        currentStrokePath = strokePath
        prepareGuidancePath()
        startAnimation()
        invalidate()
    }
    
    /**
     * Prepare the guidance path based on the current stroke path
     */
    private fun prepareGuidancePath() {
        guidancePath.reset()
        
        currentStrokePath?.let { stroke ->
            // This would parse the SVG-like path data and create a Path object
            // For now, we'll create a simple path for demonstration
            val points = stroke.guidancePoints
            if (points.isNotEmpty()) {
                guidancePath.moveTo(points[0].x, points[0].y)
                for (i in 1 until points.size) {
                    guidancePath.lineTo(points[i].x, points[i].y)
                }
            }
        }
    }
    
    /**
     * Start the guidance animation
     */
    fun startAnimation() {
        isAnimating = true
        animationProgress = 0f
        lastAnimationTime = System.currentTimeMillis()
        postInvalidateOnAnimation()
    }
    
    /**
     * Stop the guidance animation
     */
    fun stopAnimation() {
        isAnimating = false
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        currentStrokePath?.let { stroke ->
            // Draw the dotted path guide
            canvas.drawPath(guidancePath, pathPaint)
            
            // Draw guidance dots at key points
            stroke.guidancePoints.forEach { point ->
                canvas.drawCircle(point.x, point.y, 10f, dotPaint)
            }
            
            // Draw animated arrow showing direction
            drawDirectionalArrow(canvas, stroke, animationProgress)
            
            // Continue animation if active
            if (isAnimating) {
                updateAnimation()
                postInvalidateOnAnimation()
            }
        }
    }
    
    /**
     * Update the animation progress
     */
    private fun updateAnimation() {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastAnimationTime
        
        animationProgress += elapsedTime / animationDuration.toFloat()
        if (animationProgress >= 1f) {
            animationProgress = 0f
        }
        
        lastAnimationTime = currentTime
    }
    
    /**
     * Draw a directional arrow along the path based on animation progress
     */
    private fun drawDirectionalArrow(canvas: Canvas, stroke: StrokePath, progress: Float) {
        val points = stroke.guidancePoints
        if (points.size < 2) return
        
        // Calculate position along the path based on progress
        val pointIndex = (progress * (points.size - 1)).toInt()
        val nextPointIndex = (pointIndex + 1).coerceAtMost(points.size - 1)
        
        val currentPoint = points[pointIndex]
        val nextPoint = points[nextPointIndex]
        
        // Calculate arrow direction
        val dx = nextPoint.x - currentPoint.x
        val dy = nextPoint.y - currentPoint.y
        val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()
        
        // Draw arrow at current position
        canvas.save()
        canvas.translate(currentPoint.x, currentPoint.y)
        canvas.rotate(angle)
        
        // Arrow body
        canvas.drawLine(0f, 0f, 30f, 0f, arrowPaint)
        
        // Arrow head
        canvas.drawLine(30f, 0f, 20f, -10f, arrowPaint)
        canvas.drawLine(30f, 0f, 20f, 10f, arrowPaint)
        
        canvas.restore()
    }
    
    /**
     * Show the guidance overlay
     */
    fun showGuidance() {
        visibility = VISIBLE
        startAnimation()
    }
    
    /**
     * Hide the guidance overlay
     */
    fun hideGuidance() {
        stopAnimation()
        visibility = INVISIBLE
    }
}
