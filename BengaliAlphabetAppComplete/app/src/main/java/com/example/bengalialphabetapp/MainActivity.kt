package com.example.bengalialphabetapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bengalialphabetapp.databinding.ActivityMainBinding
import com.example.bengalialphabetapp.manager.GuidanceManager
import com.example.bengalialphabetapp.model.BengaliCharacter
import com.example.bengalialphabetapp.model.Point
import com.example.bengalialphabetapp.model.StrokePath

class MainActivity : AppCompatActivity(), GuidanceManager.GuidanceListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var guidanceManager: GuidanceManager
    
    // Sample Bengali character for demonstration
    private val sampleCharacter = BengaliCharacter(
        id = 1,
        unicode = "অ",
        romanizedName = "o",
        bengaliName = "অ",
        type = BengaliCharacter.CharacterType.VOWEL,
        audioResId = 0, // Would be a real resource ID in production
        strokePaths = listOf(
            StrokePath(
                id = 1,
                order = 1,
                pathData = "M50,50 C70,30 90,50 90,70 C90,90 70,100 50,90 C30,80 30,60 50,50",
                startPoint = Point(50f, 50f),
                endPoint = Point(50f, 90f),
                guidancePoints = listOf(
                    Point(50f, 50f),
                    Point(70f, 30f),
                    Point(90f, 50f),
                    Point(90f, 70f),
                    Point(70f, 100f),
                    Point(50f, 90f),
                    Point(30f, 80f),
                    Point(30f, 60f),
                    Point(50f, 50f)
                )
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize the guidance manager
        guidanceManager = GuidanceManager(
            binding.tracingCanvas,
            binding.guidanceOverlay
        )
        guidanceManager.guidanceListener = this
        
        // Set up the character for tracing
        setupCharacter()
        
        // Set up button listeners
        setupListeners()
    }
    
    private fun setupCharacter() {
        // In a real app, this would load from a database or resource
        guidanceManager.setCharacter(sampleCharacter)
        
        // For demonstration, we're using a placeholder
        // In a real app, you would load the actual character outline
        // binding.ivCharacterOutline.setImageResource(R.drawable.bengali_character_outline)
        
        // For now, we'll just show a toast to indicate the app is working
        Toast.makeText(this, "Ready to trace: " + sampleCharacter.unicode, Toast.LENGTH_SHORT).show()
    }
    
    private fun setupListeners() {
        binding.btnPronunciation.setOnClickListener {
            // In a real app, this would play the pronunciation audio
            Toast.makeText(this, "Playing pronunciation", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnNext.setOnClickListener {
            // In a real app, this would load the next character
            Toast.makeText(this, "Moving to next character", Toast.LENGTH_SHORT).show()
            binding.btnNext.isEnabled = false
            guidanceManager.reset()
            setupCharacter()
        }
    }
    
    // GuidanceManager.GuidanceListener implementation
    
    override fun onGuidanceActivated() {
        Toast.makeText(this, "Guidance activated", Toast.LENGTH_SHORT).show()
    }
    
    override fun onStrokeSuccess() {
        Toast.makeText(this, "Stroke completed successfully", Toast.LENGTH_SHORT).show()
    }
    
    override fun onStrokeFailed(attempts: Int) {
        Toast.makeText(this, "Stroke failed (Attempt $attempts)", Toast.LENGTH_SHORT).show()
    }
    
    override fun onCharacterCompleted() {
        Toast.makeText(this, "Character completed!", Toast.LENGTH_SHORT).show()
        binding.btnNext.isEnabled = true
    }
}

open class AppCompatActivity {

}
