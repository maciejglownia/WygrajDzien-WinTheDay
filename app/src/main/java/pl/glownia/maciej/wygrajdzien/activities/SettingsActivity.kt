package pl.glownia.maciej.wygrajdzien.activities

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.glownia.maciej.wygrajdzien.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private var binding: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarSettings)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Ustawienia"
        }
        binding?.toolbarSettings?.setNavigationOnClickListener {
            onBackPressed()
        }

        // Set up sending mail to developer
        binding?.llContact?.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:maciej.k.glownia@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))
        }

        // Set up button to open privacy policy
        binding?.llPrivacyPolicy?.setOnClickListener {
            val browserIntent = Intent(
                // TODO Add Privacy policy -> Intent.ACTION_VIEW, Uri.parse("link")
            )
            startActivity(browserIntent)
        }

        // Set up button to redirect user to google store to rate the app
        binding?.llRate?.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }

        // Set up button to display info about app to user as dialog
        binding?.llAbout?.setOnClickListener {
            showDescriptionDialog()
        }

        // Set up button to open my another application in Google Play Store -> Stoik Cytat Quiz
        binding?.llPhilosophy?.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=pl.glownia.maciej.stoikcytatquiz&hl=pl")
            )
            startActivity(browserIntent)
        }
    }

    private fun showDescriptionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("O aplikacji") // About application
        // TODO Short description of app -> builder.setMessage()

        builder.setNegativeButton("Zamknij") // Close
        { dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }

        val appDescription: AlertDialog = builder.create()
        appDescription.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area
        appDescription.show()  // show the dialog to UI
    }
}