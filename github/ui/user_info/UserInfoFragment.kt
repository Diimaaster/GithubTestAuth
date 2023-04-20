package com.kts.github.ui.user_info

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kts.github.BuildConfig.DEBUG
import com.kts.github.R
import com.kts.github.databinding.FragmentUserInfoBinding
import com.kts.github.utils.launchAndCollectIn
import com.kts.github.utils.resetNavGraph
import com.kts.github.utils.toast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*


class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private val viewModel: UserInfoViewModel by viewModels()
    private val binding by viewBinding(FragmentUserInfoBinding::bind)

    private val logoutResponse = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.webLogoutComplete()
        } else {
            // логаут отменен
            // делаем complete тк github не редиректит после логаута и пользователь закрывает CCT
            viewModel.webLogoutComplete()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.corruptAccessToken.setOnClickListener {
//            viewModel.corruptAccessToken()
//        }
        binding.getUserInfo.setOnClickListener {
            viewModel.loadUserInfo()
        }
        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.loadingFlow.launchAndCollectIn(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.getUserInfo.isEnabled = !isLoading
            binding.userInfo.isVisible = !isLoading
            binding.imageView.isVisible = !isLoading
            binding.userName.isVisible = !isLoading
        }

        viewModel.userInfoFlow.launchAndCollectIn(viewLifecycleOwner) { userInfo ->
            Picasso.get().load(userInfo?.avatar_url).into(binding.imageView)
            binding.userName.text = "Логин: " + userInfo?.login
            binding.userInfo.text =
                " \nОткрытых репозиториев: " + userInfo?.public_repos + " \nПодписчиков: " + userInfo?.followers + " \nПодписок: " + userInfo?.following

        }

        viewModel.userRepoFlow.launchAndCollectIn(viewLifecycleOwner) { userRepo ->
            val items = userRepo?.body()
            if (items != null) {
                for (i in 0 until items.count()) {

                    binding.name.text = binding.name.text.toString() + "\n" + items[i].name ?: "N/A"
                    Log.d("Название: ", items[i].name)

                    binding.language.text = binding.language.text.toString() + "\n" + items[i].language ?: "N/A"
                    Log.d("Язык: ", items[i].language)

                    binding.create.text =binding.create.text.toString() + "\n" + items[i].created_at ?: "N/A"
                    Log.d("Создан: ", items[i].created_at)

                    binding.infoupdt.text = binding.infoupdt.text.toString() + "\n" + items[i].updated_at ?: "N/A"
                    Log.d("Обновлен: ", items[i].updated_at)

                }
                println(" !!!!!!!   ПРОВЕРКА      !   :" + items[1].name)
            }

            viewModel.toastFlow.launchAndCollectIn(viewLifecycleOwner) {
                toast(it)
            }

            viewModel.logoutPageFlow.launchAndCollectIn(viewLifecycleOwner) {
                logoutResponse.launch(it)
            }

            viewModel.logoutCompletedFlow.launchAndCollectIn(viewLifecycleOwner) {
                findNavController().resetNavGraph(R.navigation.nav_graph)
            }
        }
    }
}