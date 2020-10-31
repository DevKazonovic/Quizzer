package com.my.projects.quizapp.presentation.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.my.projects.quizapp.data.remote.QuizApi
import com.my.projects.quizapp.databinding.FragmentQuizBinding
import com.my.projects.quizapp.util.Const.Companion.KEY_AMOUNT
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.Const.Companion.KEY_DIFFICULTY
import com.my.projects.quizapp.util.Const.Companion.KEY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class QuizFragment : Fragment() {

    private lateinit var quizBinding: FragmentQuizBinding
    private var amount:Int=10
    private var category:Int?=null
    private var difficulty:String?=null
    private var type:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category=it.getInt(KEY_CATEGORY)
            amount= it.getInt(KEY_AMOUNT)
            difficulty=it.getString(KEY_DIFFICULTY)
            type=it.getString(KEY_TYPE)
            Timber.d("cat: $category, amount: $amount, difficulty: $difficulty, type:$type")


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizBinding= FragmentQuizBinding.inflate(inflater)

        lifecycleScope.launch {
            var result: String
            withContext(Dispatchers.IO){
                 val response=QuizApi.quizAPI.getQuiz(amount,category,difficulty,type)
                result=response.results.toString()
            }
            quizBinding.textView.text=result
        }

        return quizBinding.root
    }


}