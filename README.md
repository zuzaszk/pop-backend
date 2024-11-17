## Authorization Using Roles in Spring Security

In the backend, role-based authorization is implemented using the `@PreAuthorize` annotation. Roles are mapped as `ROLE_<role_name>` (e.g., `ROLE_CHAIR`, `ROLE_SUPERVISOR`). 

### Example Usage

For restricting access to a method based on roles, use the following:

```java
@PreAuthorize("hasRole('ROLE_CHAIR')")
public ResponseEntity<List<User>> listAll() {
    // Only accessible by users with the 'CHAIR' role
    return ResponseEntity.ok(userService.getAllUsers());
}
