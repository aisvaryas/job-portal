# Job Seeker Login - Jobseeker Table Update Fix

## Problem
When a job seeker logs in, the jobseeker table was not being updated. New job seekers would register but no jobseeker record would be created, causing profile loading to fail.

## Root Cause
The AuthController's login endpoint only authenticated users and returned a token, but never created or ensured a Jobseeker record existed. Jobseeker records were only created by explicitly calling the `/jobseeker/save` endpoint with full details.

## Solution Implemented

### 1. **Auto-Create Jobseeker on Login**
Modified `AuthController.login()` to automatically create a jobseeker profile if one doesn't exist:
```java
// After successful authentication
if (normalizedRole == Role.JOB_SEEKER) {
    jobseekerService.ensureJobseekerExists(user);
}
```

### 2. **Added ensureJobseekerExists() Method**
Created new method in `JobseekerService` that:
- Checks if a jobseeker record already exists for the user
- Creates a new record with auto-generated default values if it doesn't exist
- Extracts name from email if not provided (e.g., john.doe@email.com → John Doe)
- Sets reasonable defaults:
  - **firstName**: Extracted from email prefix
  - **lastName**: Extracted from email prefix
  - **phone**: null (optional, user can update)
  - **location**: Empty string
  - **headline**: Empty string
  - **isActive**: true

### 3. **Made Phone Field Optional**
Updated `Jobseeker.java`:
- Changed phone column from `nullable=false` to `nullable=true`
- This allows new jobseekers to be created without requiring phone number validation
- Users can update phone number later in their profile

### 4. **Added Repository Method**
Added `findByUser()` method to `JobSeekerRepo` for efficient lookup:
```java
Optional<Jobseeker> findByUser(User user);
```

### 5. **Added Transactional Support**
Added `@Transactional` annotations to all CRUD methods in JobseekerService for data consistency.

## Files Modified

1. **AuthController.java**
   - Added JobseekerService dependency
   - Added call to `ensureJobseekerExists()` after successful JOB_SEEKER login

2. **JobseekerService.java**
   - Added `ensureJobseekerExists(User user)` method
   - Added `@Transactional` annotations to all CRUD operations
   - Added `Optional` import

3. **JobSeekerRepo.java**
   - Added `findByUser(User user)` query method

4. **Jobseeker.java**
   - Made `phone` field nullable (changed `nullable=false` to `nullable=true`)

## Login Flow After Fix

```
User Registration
    ↓
User record created (email, password, role=JOB_SEEKER)
    ↓
User Login
    ↓
AuthController validates credentials
    ↓
Check role = JOB_SEEKER?
    ├─ YES → Call ensureJobseekerExists(user)
    │         ├─ Check if jobseeker exists
    │         └─ If not, create with defaults
    │
    └─ NO → Continue
    ↓
Generate JWT token
    ↓
Return login response
    ↓
Frontend stores token & user info
    ↓
Frontend navigates to /dashboard/seeker
    ↓
Profile page loads jobseeker data
    ↓
Jobseeker record now exists ✅
```

## Benefits

✅ Jobseeker table is updated immediately on first login
✅ No additional API calls needed after login
✅ Auto-populated profile fields from email
✅ Phone field is optional - user can add it later
✅ Transactional consistency ensures data integrity
✅ Efficient database lookup using findByUser()
✅ Profile page will find jobseeker data on first load

## Testing

### Test Case 1: New Job Seeker
1. Register new account as JOB_SEEKER with email `john@example.com`
2. Login with those credentials
3. Check database: `SELECT * FROM jobseeker` should show a new row with:
   - firstName: "John"
   - lastName: "Example"  
   - phone: NULL
   - isActive: 1

### Test Case 2: Existing Job Seeker
1. Logout and login again
2. Verify same jobseeker record is returned (no duplicate creation)

### Test Case 3: Profile Page Load
1. Login as job seeker
2. Navigate to /dashboard/seeker
3. Profile should load successfully with the auto-created data
4. User can update phone, location, headline

## Backward Compatibility

✅ Existing job seekers with jobseeker records are unaffected
✅ Existing job seekers without records will get one on next login
✅ Phone field being nullable doesn't break existing data
✅ All endpoints continue to work as before

## Potential Future Improvements

- Add first/last name fields to registration form to avoid email parsing
- Add phone number to registration and populate on account creation
- Send welcome email after auto-profile creation
- Add profile completion wizard to encourage users to add more details
