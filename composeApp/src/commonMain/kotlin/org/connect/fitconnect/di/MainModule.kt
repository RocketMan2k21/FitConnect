package org.connect.fitconnect.di

import org.connect.fitconnect.FitDatabase
import org.connect.fitconnect.cache.DriverFactory
import org.connect.fitconnect.cache.createDatabase
import org.connect.fitconnect.domain.workout.ExerciseRepository
import org.connect.fitconnect.domain.workout.MuscleGroupRepository
import org.connect.fitconnect.domain.workout.SetRepository
import org.connect.fitconnect.domain.workout.WorkoutRepository
import org.connect.fitconnect.domain.workout.usecase.GetAllWorkoutUseCase
import org.connect.fitconnect.domain.workout.usecase.GetSetsForWorkout
import org.connect.fitconnect.domain.workout.usecase.InitiateNewWorkoutUseCase
import org.connect.fitconnect.workout.data.exercise.ExerciseDataSource
import org.connect.fitconnect.workout.data.exercise.ExerciseDataSourceSql
import org.connect.fitconnect.workout.data.exercise.ExerciseRepositorySQL
import org.connect.fitconnect.workout.data.exercise.ExerciseViewModel
import org.connect.fitconnect.workout.data.exercise.NewExerciseViewModel
import org.connect.fitconnect.workout.data.history.SetHistoryViewModel
import org.connect.fitconnect.workout.data.muscle.MuscleDataSource
import org.connect.fitconnect.workout.data.muscle.MuscleDataSourceSql
import org.connect.fitconnect.workout.data.muscle.MuscleRepositorySql
import org.connect.fitconnect.workout.data.set.SetDataSource
import org.connect.fitconnect.workout.data.set.SetDataSourceSql
import org.connect.fitconnect.workout.data.set.SetRepositorySql
import org.connect.fitconnect.workout.data.set.SetViewModel
import org.connect.fitconnect.workout.data.workout.HomeScreenUseCases
import org.connect.fitconnect.workout.data.workout.HomeScreenViewModel
import org.connect.fitconnect.workout.data.workout.WorkoutDataSource
import org.connect.fitconnect.workout.data.workout.WorkoutDataSourceSql
import org.connect.fitconnect.workout.data.workout.WorkoutRepositorySql
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single<FitDatabase> { createDatabase(DriverFactory())}
    single<ExerciseDataSource>{ExerciseDataSourceSql(get())}
    single<WorkoutDataSource>{WorkoutDataSourceSql(get())}
    single<MuscleDataSource>{MuscleDataSourceSql(get())}
    single<SetDataSource>{SetDataSourceSql(get())}
    single<WorkoutRepository> { WorkoutRepositorySql(get()) }
    single<ExerciseRepository> { ExerciseRepositorySQL(get()) }
    single<SetRepository> { SetRepositorySql(get()) }
    single<MuscleGroupRepository> { MuscleRepositorySql(get()) }
    factory {
        HomeScreenViewModel(
            HomeScreenUseCases(
                getSetsForWorkout = GetSetsForWorkout(get()),
                getAllWorkoutsUseCase = GetAllWorkoutUseCase(get()),
                initiateNewWorkout = InitiateNewWorkoutUseCase(get())
            )
        )
    }
    factory {
        ExerciseViewModel(
            exerciseRepository = get()
        )
    }
    factory { SetViewModel (
        exerciseRepository = get(),
        setRepository = get()
    )}
    factory { SetHistoryViewModel(get()) }
    factory { NewExerciseViewModel(get(), get()) }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}