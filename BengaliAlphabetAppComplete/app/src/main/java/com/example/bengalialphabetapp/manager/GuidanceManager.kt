package com.example.bengalialphabetapp.manager

import com.example.bengalialphabetapp.model.BengaliCharacter
import com.example.bengalialphabetapp.views.GuidanceOverlayView
import com.example.bengalialphabetapp.views.TracingCanvasView

/**
 * Manager class that handles the guidance system for tracing Bengali characters.
 * It monitors tracing attempts and activates guidance when needed.
 */
class GuidanceManager(
    private val tracingCanvasView: TracingCanvasView,
    private val guidanceOverlayView: GuidanceOverlayView
) : TracingCanvasView.TracingListener {

    // Current character being traced
    private var currentCharacter: BengaliCharacter? = null
    
    // Current stroke index
    private var currentStrokeIndex = 0
    
    // Failed attempt counters
    private var failedAttempts = 0
    private val maxFailedAttempts = 3
    
    // Callback for guidance events
    var guidanceListener: GuidanceListener? = null
    
    init {
        tracingCanvasView.tracingListener = this
    }
    
    /**
     * Set the current character for tracing and guidance
     */
    fun setCharacter(character: BengaliCharacter) {
        currentCharacter = character
        currentStrokeIndex = 0
        failedAttempts = 0
        
        // Set the reference stroke paths for tracing
        tracingCanvasView.setReferenceStrokePaths(character.strokePaths)
        
        // Hide guidance initially
        guidanceOverlayView.hideGuidance()
    }
    
    /**
     * Reset the guidance system for a new attempt
     */
    fun reset() {
        failedAttempts = 0
        guidanceOverlayView.hideGuidance()
        tracingCanvasView.resetCanvas()
    }
    
    /**
     * Show guidance for the current stroke
     */
    private fun showGuidance() {
        currentCharacter?.let { character ->
            if (currentStrokeIndex < character.strokePaths.size) {
                val currentStroke = character.strokePaths[currentStrokeIndex]
                guidanceOverlayView.setGuidanceForStroke(currentStroke)
                guidanceOverlayView.showGuidance()
                guidanceListener?.onGuidanceActivated()
            }
        }
    }
    
    /**
     * Hide guidance
     */
    private fun hideGuidance() {
        guidanceOverlayView.hideGuidance()
    }
    
    // TracingCanvasView.TracingListener implementation
    
    override fun onStrokeStarted() {
        // Hide guidance when user starts a new stroke
        hideGuidance()
    }
    
    override fun onStrokeProgress(progress: Float) {
        // Real-time feedback could be provided here
    }
    
    override fun onStrokeCompleted(isValid: Boolean) {
        if (isValid) {
            // Reset failed attempts counter on success
            failedAttempts = 0
            currentStrokeIndex++
            guidanceListener?.onStrokeSuccess()
        } else {
            // Increment failed attempts counter
            failedAttempts++
            guidanceListener?.onStrokeFailed(failedAttempts)
            
            // Show guidance after reaching max failed attempts
            if (failedAttempts >= maxFailedAttempts) {
                showGuidance()
            }
        }
    }
    
    override fun onCharacterCompleted() {
        hideGuidance()
        guidanceListener?.onCharacterCompleted()
    }
    
    /**
     * Interface for guidance event callbacks
     */
    interface GuidanceListener {
        fun onGuidanceActivated()
        fun onStrokeSuccess()
        fun onStrokeFailed(attempts: Int)
        fun onCharacterCompleted()
    }
}
