package com.aatif.tchello.composition.activity

import androidx.appcompat.app.AppCompatActivity
import com.aatif.tchello.screens.add_board.AddBoardActivity
import com.aatif.tchello.screens.common.BaseActivity
import com.aatif.tchello.screens.homepage.HomePageActivity
import com.aatif.tchello.screens.intro.IntroActivity
import com.aatif.tchello.screens.profile.ProfileActivity
import com.aatif.tchello.screens.signin.SignInActivity
import com.aatif.tchello.screens.signup.SignUpActivity
import com.aatif.tchello.screens.splash_screen.SplashActivity
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: SplashActivity)

    fun inject(activity: IntroActivity)

    fun inject(activity: SignInActivity)

    fun inject(activity: SignUpActivity)

    fun inject(activity: HomePageActivity)

    fun inject(activity: ProfileActivity)
    fun inject(addBoardActivity: AddBoardActivity)

    @Subcomponent.Builder
    interface Builder{

        @BindsInstance
        fun activity(activity: BaseActivity<*>):Builder

        fun build(): ActivityComponent
    }
}