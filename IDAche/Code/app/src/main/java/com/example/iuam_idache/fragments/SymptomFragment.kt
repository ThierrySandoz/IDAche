package com.example.iuam_idache.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.HeadachePages
import com.example.iuam_idache.classes.NumberPickerSharedViewModel

class SymptomFragment : Fragment() {

    //-------------- TextViews
    private lateinit var symptomDescription : TextView
    private lateinit var symptomLevelDescription : TextView

    //-------------- ImageViews
    private lateinit var symptomImage : ImageView

    //-------------- itemSelector
    private lateinit var model : NumberPickerSharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the view
        val view = inflater.inflate(R.layout.fragment_headache_symptom, container, false)

        //----------------- ImageViews
        symptomImage = view.findViewById(R.id.headache_coffee_imageView)

        //----------------- TextViews
        symptomLevelDescription = view.findViewById(R.id.headache_coffee_level_description_textView)
        symptomDescription = view.findViewById(R.id.headache_coffee_description_textView)

        //----------------- Shared view model
        model = ViewModelProviders.of(requireActivity()).get(NumberPickerSharedViewModel::class.java)

        model.selectedItem.observe(viewLifecycleOwner, { t ->
            when(model.actualPage.value) {
                //================================= COFFEE =========================================
                HeadachePages.COFFEE -> {
                    symptomDescription.text = getString(R.string.select_the_amount_of_coffee_consumed_today)
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_1)
                            symptomLevelDescription.text = "Few : 1 coffee"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_2)
                            symptomLevelDescription.text = "Few : 2 coffees"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_3)
                            symptomLevelDescription.text = "Medium : 3 coffees"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_4)
                            symptomLevelDescription.text = "Medium : 4 coffees"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_5)
                            symptomLevelDescription.text = "Medium : 5 coffees"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_6)
                            symptomLevelDescription.text = "A lot : 6 coffees"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_7)
                            symptomLevelDescription.text = "A lot : 7 coffees"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_8)
                            symptomLevelDescription.text = "A lot : 8 coffees"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_coffee_animation_9)
                            symptomLevelDescription.text = "A lot : More than 9 coffees"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_coffee_animation_5)
                    }
                }
                //=============================== PAIN LEVEL =======================================
                HeadachePages.PAIN_LEVEL -> {
                    symptomDescription.text = getString(R.string.indicate_according_to_you_on_a_scale_from_1_to_9_what_is_the_level_of_pain_of_your_headache)
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_1)
                            symptomLevelDescription.text = getString(R.string.tolerable)
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_2)
                            symptomLevelDescription.text = getString(R.string.Discomforting)
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_3)
                            symptomLevelDescription.text = getString(R.string.distressing)
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_4)
                            symptomLevelDescription.text = getString(R.string.very_distressing)
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_5)
                            symptomLevelDescription.text = getString(R.string.intense)
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_6)
                            symptomLevelDescription.text = getString(R.string.very_intense)
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_7)
                            symptomLevelDescription.text = getString(R.string.horrible)
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_8)
                            symptomLevelDescription.text = getString(R.string.unbearable)
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_pain_level_9)
                            symptomLevelDescription.text = getString(R.string.unspeakable)
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_pain_level_5)
                    }
                }

                //============================= HEADACHE AREA ======================================
                HeadachePages.HEADACHE_AREA -> {
                    symptomDescription.text = getString(R.string.headache_positioning_description)
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_cluster)
                            symptomLevelDescription.text = "Cluster"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_sinus)
                            symptomLevelDescription.text = "Sinus"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_tmj)
                            symptomLevelDescription.text = "TMJ"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_migraine)
                            symptomLevelDescription.text = "Migraine"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_tension)
                            symptomLevelDescription.text = "Tension"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_stress)
                            symptomLevelDescription.text = "Stress"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_allergy)
                            symptomLevelDescription.text = "Allergy"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_hypertension)
                            symptomLevelDescription.text = "Hypertension"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.headache_type_cervical)
                            symptomLevelDescription.text = "Cervical"
                        }
                        else -> symptomImage.setImageResource(R.drawable.headache_type_tension)
                    }
                }

                //=============================== CIGARETTE ========================================
                HeadachePages.CIGARETTE -> {
                    symptomDescription.text = getString(R.string.number_of_cigarettes_smoked_description)
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_1)
                            symptomLevelDescription.text = "Cigarette 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_2)
                            symptomLevelDescription.text = "Cigarette 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_3)
                            symptomLevelDescription.text = "Cigarette 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_4)
                            symptomLevelDescription.text = "Cigarette 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_5)
                            symptomLevelDescription.text = "Cigarette 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_6)
                            symptomLevelDescription.text = "Cigarette 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_7)
                            symptomLevelDescription.text = "Cigarette 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_8)
                            symptomLevelDescription.text = "Cigarette 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette_9)
                            symptomLevelDescription.text = "Cigarette 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_cigarette_5)
                    }
                }

                //================================ ALCOHOL =========================================
                HeadachePages.ALCOHOL -> {
                    symptomDescription.text = getString(R.string.amount_of_alcool_description)
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_bier)
                            symptomLevelDescription.text = "Alcohol 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_bier)
                    }
                }
                HeadachePages.DIZZINESS -> {
                    symptomDescription.text = "Indicate according to you how much on a 1 to 9 scale is your dizziness"
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_dizziness)
                            symptomLevelDescription.text = "Dizziness 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_dizziness)
                    }
                }
                HeadachePages.TEMPERATURE -> {
                    symptomDescription.text = "Indicate according to you how much on a 1 to 9 scale is your temperature"
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_high_temperature)
                            symptomLevelDescription.text = "Temperature 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_high_temperature)
                    }
                }
                HeadachePages.TOOTHACHE -> {
                    symptomDescription.text = "Indicate according to you how much on a 1 to 9 scale is your toothache"
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_toothache)
                            symptomLevelDescription.text = "Toothache 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_toothache)
                    }
                }
            }

        })

        // Return the fragment view/layout
        return view
    }
}