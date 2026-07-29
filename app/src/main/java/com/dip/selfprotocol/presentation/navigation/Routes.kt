package com.dip.selfprotocol.presentation.navigation

sealed class Route(val route: String) {
    object Home : Route("home")
    object Categories : Route("categories/{type}") {
        fun createRoute(type: String) = "categories/$type"
    }
    object RulesList : Route("rules_list/{categoryId}") {
        fun createRoute(categoryId: Int) = "rules_list/$categoryId"
    }
    object RuleDetail : Route("rule_detail/{categoryId}?ruleId={ruleId}") {
        fun createRoute(categoryId: Int, ruleId: Int? = null) = 
            if (ruleId != null) "rule_detail/$categoryId?ruleId=$ruleId" else "rule_detail/$categoryId"
    }
    object LessonsList : Route("lessons_list/{categoryId}") {
        fun createRoute(categoryId: Int) = "lessons_list/$categoryId"
    }
    object LessonDetail : Route("lesson_detail/{categoryId}?lessonId={lessonId}") {
        fun createRoute(categoryId: Int, lessonId: Int? = null) = 
            if (lessonId != null) "lesson_detail/$categoryId?lessonId=$lessonId" else "lesson_detail/$categoryId"
    }
    object Settings : Route("settings")
    object Lock : Route("lock")
}
