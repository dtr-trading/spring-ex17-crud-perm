-- Create strategies
call createStrategy('Iron Condor', 'High Prob IC');
call createStrategy('Iron Condor', 'Low Prob IC');
call createStrategy('Iron Condor', 'High Prob Hedged IC');
call createStrategy('Butterfly', 'Bullish Leaning Butter');
call createStrategy('Calendar', 'Double Cal');
call createStrategy('Double Diagonal', 'Bullish');

-- Create permissions

call createPermission('CTRL_STRATEGY_LIST_GET');
call createPermission('CTRL_STRATEGY_ADD_POST');
call createPermission('CTRL_STRATEGY_EDIT_GET');
call createPermission('CTRL_STRATEGY_EDIT_POST');
call createPermission('CTRL_STRATEGY_DELETE_GET');

call createPermission('CTRL_USER_LIST_GET');
call createPermission('CTRL_USER_ADD_POST');
call createPermission('CTRL_USER_EDIT_GET');
call createPermission('CTRL_USER_EDIT_POST');
call createPermission('CTRL_USER_DELETE_GET');

call createPermission('CTRL_ROLE_LIST_GET');
call createPermission('CTRL_ROLE_ADD_POST');
call createPermission('CTRL_ROLE_EDIT_GET');
call createPermission('CTRL_ROLE_EDIT_POST');
call createPermission('CTRL_ROLE_DELETE_GET');

call createPermission('CTRL_PERM_LIST_GET');
call createPermission('CTRL_PERM_ADD_POST');
call createPermission('CTRL_PERM_EDIT_GET');
call createPermission('CTRL_PERM_EDIT_POST');
call createPermission('CTRL_PERM_DELETE_GET');


-- Create roles

call createRole('ROLE_ADMIN', @role_admin);
call createRoleHasPermission(@role_admin, 'CTRL_STRATEGY_LIST_GET');
call createRoleHasPermission(@role_admin, 'CTRL_STRATEGY_ADD_POST');
call createRoleHasPermission(@role_admin, 'CTRL_STRATEGY_EDIT_GET');
call createRoleHasPermission(@role_admin, 'CTRL_STRATEGY_EDIT_POST');
call createRoleHasPermission(@role_admin, 'CTRL_STRATEGY_DELETE_GET');

call createRoleHasPermission(@role_admin, 'CTRL_USER_LIST_GET');
call createRoleHasPermission(@role_admin, 'CTRL_USER_ADD_POST');
call createRoleHasPermission(@role_admin, 'CTRL_USER_EDIT_GET');
call createRoleHasPermission(@role_admin, 'CTRL_USER_EDIT_POST');
call createRoleHasPermission(@role_admin, 'CTRL_USER_DELETE_GET');

call createRoleHasPermission(@role_admin, 'CTRL_ROLE_LIST_GET');
call createRoleHasPermission(@role_admin, 'CTRL_ROLE_ADD_POST');
call createRoleHasPermission(@role_admin, 'CTRL_ROLE_EDIT_GET');
call createRoleHasPermission(@role_admin, 'CTRL_ROLE_EDIT_POST');
call createRoleHasPermission(@role_admin, 'CTRL_ROLE_DELETE_GET');

call createRoleHasPermission(@role_admin, 'CTRL_PERM_LIST_GET');
call createRoleHasPermission(@role_admin, 'CTRL_PERM_ADD_POST');
call createRoleHasPermission(@role_admin, 'CTRL_PERM_EDIT_GET');
call createRoleHasPermission(@role_admin, 'CTRL_PERM_EDIT_POST');
call createRoleHasPermission(@role_admin, 'CTRL_PERM_DELETE_GET');

call createRole('ROLE_TRADER', @role_trader);

call createRole('ROLE_USER', @role_user);


-- Create accounts

call createUser('admin', @admin);
call createUserHasRole(@admin, @role_admin);

call createUser('trader', @trader);
call createUserHasRole(@trader, @role_trader);

call createUser('user', @user);
call createUserHasRole(@user, @role_user);
