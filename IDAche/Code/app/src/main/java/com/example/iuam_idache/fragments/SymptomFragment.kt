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

        model.actualPage.observe(viewLifecycleOwner, { t ->
            when (t) {
                HeadachePages.PAIN_LEVEL -> {
                    symptomDescription.text = getString(R.string.indicate_according_to_you_on_a_scale_from_1_to_9_what_is_the_level_of_pain_of_your_headache)
                    symptomImage.setImageResource(R.drawable.ic_headache)
                }
                HeadachePages.HEADACHE_AREA -> TODO()
                HeadachePages.COFFEE -> {
                    symptomDescription.text = getString(R.string.select_the_amount_of_coffee_consumed_today)
                    symptomImage.setImageResource(R.drawable.ic_coffee_animation_5)
                }
                HeadachePages.CIGARETTE -> {
                    symptomDescription.text = getString(R.string.number_of_cigarettes_smoked_description)
                    symptomImage.setImageResource(R.drawable.ic_cigarette)
                }
                HeadachePages.ALCOHOL -> {
                    symptomDescription.text = getString(R.string.amount_of_alcool_description)
                    symptomImage.setImageResource(R.drawable.ic_bier)
                }
                HeadachePages.DIZZINESS -> TODO()
                HeadachePages.TEMPERATURE -> TODO()
                HeadachePages.TOOTHACHE -> TODO()
            }
        })

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
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Pain level 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_headache)
                    }
                }

                //============================= HEADACHE AREA ======================================
                HeadachePages.HEADACHE_AREA -> {
                    symptomDescription.text = getString(R.string.headache_positioning_description)
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_headache)
                            symptomLevelDescription.text = "Position 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_headache)
                    }
                }

                //=============================== CIGARETTE ========================================
                HeadachePages.CIGARETTE -> {
                    symptomDescription.text = getString(R.string.number_of_cigarettes_smoked_description)
                    when(t) {
                        1 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 1"
                        }
                        2 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 2"
                        }
                        3 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 3"
                        }
                        4 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 4"
                        }
                        5 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 5"
                        }
                        6 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 6"
                        }
                        7 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 7"
                        }
                        8 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 8"
                        }
                        9 -> {
                            symptomImage.setImageResource(R.drawable.ic_cigarette)
                            symptomLevelDescription.text = "Cigarette 9"
                        }
                        else -> symptomImage.setImageResource(R.drawable.ic_cigarette)
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
                HeadachePages.DIZZINESS -> TODO()
                HeadachePages.TEMPERATURE -> TODO()
                HeadachePages.TOOTHACHE -> TODO()
            }

        })

        // Return the fragment view/layout
        return view
    }
}