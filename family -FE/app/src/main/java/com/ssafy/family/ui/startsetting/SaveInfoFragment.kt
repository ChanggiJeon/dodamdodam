package com.ssafy.family.ui.startsetting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.FamilyReq
import com.ssafy.family.databinding.FragmentSaveInfoBinding
import com.ssafy.family.ui.status.StatusActivity
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.FileUtils
import com.ssafy.family.util.InputValidUtil
import com.ssafy.family.util.SharedPreferencesUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SaveInfoFragment : Fragment() {

    private lateinit var binding: FragmentSaveInfoBinding
    private val familyViewModel by activityViewModels<StartSettingViewModel>()

    // 이미지 업로드 관련 변수들
    var imageUri: Uri? = null
    lateinit var imagePickerLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 상단 텍스트 수정
        (activity as StartSettingActivity).changeTopMessage("'나'를 저장하세요!")
        // 스피너(다이얼로그 형) 설정
        val roleData = resources.getStringArray(R.array.family_role)
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, roleData)
        binding.saveInfoSpinner.adapter = adapter
        binding.saveInfoSpinner.setSelection(0)
        binding.saveInfoSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d(TAG, "선택한 role : " + binding.saveInfoSpinner.selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        // 이미지 선택 런처 등록
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    imageUri = it.data?.data
                    Glide.with(activity as StartSettingActivity)
                        .load(imageUri)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.image_fail)
                        .fallback(R.drawable.image_fail)
                        .into(binding.saveInfoProfileImage)
                }
            }
        // 이미지 선택 리스너 등록
        binding.saveInfoProfileImage.setOnClickListener {
            getProfileImage()
        }
        // Data 관련 코드
        initView()
    } // onViewCreated

    private fun initView() {
        // "다음" 버튼 클릭 리스너 등록
        // 가족 생성
        binding.saveInfoMoveNextBtn.setOnClickListener {
            if (isValidForm()) {
                createFamily()
            }
        }
        // 닉네임 유효성 검사 통과 시 에러메시지 삭제
        binding.saveInfoInputNickname.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidNickName(input)) {
                dismissErrorOnNickName()
            }
        }
        // 생년월일 유효성 검사 통과 시 에러메시지 삭제
        binding.saveInfoInputBirthday.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidBirthDay(input)) {
                dismissErrorOnBirthday()
            }
        }

        // LiveData observe
        familyViewModel.familyResponseLiveData.observe(requireActivity()) {
            if (it.status == Status.SUCCESS){
                SharedPreferencesUtil(requireContext()).setString("familyId", it.data?.dataset?.familyId.toString())
                startActivity(Intent(requireContext(), StatusActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "프로필 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }


    } // initView

    // 데이터 유효성 검사
    private fun isValidForm(): Boolean {
        val nickname = binding.saveInfoInputNickname.text.toString()
        val birthday = binding.saveInfoInputBirthday.text.toString()
        var flag = 1
        // 닉네임 유효성 검사
        if (InputValidUtil.isValidNickName(nickname)) {
            dismissErrorOnNickName()
        } else {
            flag = 0
            setErrorOnNickName()
        }
        // 생년월일 유효성 검사
        if (InputValidUtil.isValidBirthDay(birthday)) {
            dismissErrorOnBirthday()
        } else {
            flag = 0
            setErrorOnBirthday()
        }
        //유효성 검사 모두 통과 시 flag == 1 (-> true)
        return flag == 1
    }

    // 에러메시지 표시와 관련된 함수들
    private fun setErrorOnNickName() {
        binding.textInputLayoutSaveInfoNickname.error =
            resources.getString(R.string.nickNameErrorMessage)
    }

    private fun dismissErrorOnNickName() {
        binding.textInputLayoutSaveInfoNickname.error = null
    }

    private fun setErrorOnBirthday() {
        binding.textInputLayoutSaveInfoBirthday.error =
            resources.getString(R.string.birthdayErrorMessage)
    }

    private fun dismissErrorOnBirthday() {
        binding.textInputLayoutSaveInfoBirthday.error = null
    }

    // ***중요*** 네트워크(profile 데이터 보내고 familyId 받아오는 함수)
    private fun createFamily() {
        val role = binding.saveInfoSpinner.selectedItem.toString()
        val nickname = binding.saveInfoInputNickname.text.toString()
        val birthday = InputValidUtil.makeDay(binding.saveInfoInputBirthday.text.toString())
//        val imageFile = imageUriToFile(imageUri)
        val imageFile = FileUtils.getFile(requireContext(), imageUri!!)

        familyViewModel.createFamily(FamilyReq(role, nickname, birthday), imageFile)
    }

    // 이미지 선택 런쳐 실행 함수
    private fun getProfileImage() {
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        chooserIntent.putExtra(Intent.EXTRA_INTENT, intent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "사용할 앱을 선택해주세요.")
        imagePickerLauncher.launch(chooserIntent)
    }

    // 이미지 Uri -> File
    private fun imageUriToFile(uri: Uri?): File? {
        var uri: Uri? = uri
        Log.d(TAG, "SaveInfoFragment - imageUriToFile() uri = $uri")
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        var cursor: Cursor? = (activity as StartSettingActivity).contentResolver.query(
            uri!!,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        )
        if (cursor == null || cursor.columnCount < 1) {
            return null
        }
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path: String = cursor.getString(column_index)
        if (cursor != null) {
            cursor.close()
            cursor = null
        }
        Log.d(TAG, "SaveInfoFragment - imageUriToFile() path = $path")
        return File(path)
    }
}
