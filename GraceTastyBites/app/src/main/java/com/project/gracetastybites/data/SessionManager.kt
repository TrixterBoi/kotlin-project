package com.project.gracetastybites.data

import com.project.gracetastybites.data.db.StaffDAO

enum class UserRole { ADMIN, STAFF, CUSTOMER }

object SessionManager {
    private var _currentUser: StaffDAO? = null
    private var _userRole: UserRole = UserRole.CUSTOMER
    private var listeners = mutableSetOf<() -> Unit>()

    val currentUser: StaffDAO? get() = _currentUser
    val userRole: UserRole get() = _userRole

    fun login(user: StaffDAO) {
        _currentUser = user
        _userRole = when (user.Role.lowercase()) {
            "admin" -> UserRole.ADMIN
            "staff" -> UserRole.STAFF
            else -> UserRole.CUSTOMER
        }
        notifyListeners()
    }

    fun logout() {
        _currentUser = null
        _userRole = UserRole.CUSTOMER
        notifyListeners()
    }

    fun isLoggedIn(): Boolean = _currentUser != null

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }
}