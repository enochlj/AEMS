package cn.hlj.ems.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hlj.ems.daos.DepartmentDao;
import cn.hlj.ems.daos.RoleDao;
import cn.hlj.ems.entities.Department;
import cn.hlj.ems.entities.Employee;
import cn.hlj.ems.entities.Role;
import cn.hlj.ems.exceptions.EmployeeDisabledException;
import cn.hlj.ems.exceptions.EmployeeIsDeletedException;
import cn.hlj.ems.exceptions.LoginNameNotFoundException;
import cn.hlj.ems.exceptions.PasswordNotMatchException;

@Service
public class EmployeeService extends BaseService<Employee> {
	/*
	 * @Autowired private EmployeeDao employeeDao;
	 */

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private RoleDao roleDao;

	@Transactional
	public Employee login(String loginName, String password) {
		Employee employee = baseDao.getBy("loginName", loginName);
		// 判断用户是否存在
		if (employee == null) {
			throw new LoginNameNotFoundException();
		}
		// 判断用户是否被删除
		if (employee.getIsDeleted() == 1) {
			throw new EmployeeIsDeletedException();
		}
		// 判断该用户是否被禁用
		if (employee.getEnabled() != 1) {
			throw new EmployeeDisabledException();
		}
		// 判断输入的密码和数据表中的密码是否一致
		if (!password.equals(employee.getPassword())) {
			throw new PasswordNotMatchException();
		}
		// 若验证通过, 则登陆次数 + 1
		employee.setVisitedTimes(employee.getVisitedTimes() + 1);
		return employee;
	}

	/*
	 * @Transactional(readOnly = true) public Page<Employee> getPage(int pageNo)
	 * { return baseDao.getPage(pageNo); }
	 */

	@Override
	protected void initializePageContent(List<Employee> content) {
		for (Employee employee : content) {
			Hibernate.initialize(employee.getDepartment());
			Hibernate.initialize(employee.getRoles());
		}
	}

	@Transactional
	public int delete(Integer id) {
		// 查询id对应的Employee是否为一个Manager
		// 如果是，则返回0
		Employee manager = new Employee();
		manager.setEmployeeId(id);
		Department department = departmentDao.getBy("manager", manager);
		if (department != null) {
			return 0;
		}

		// 若不是 Manager, 则执行删除. 且返回 1
		Employee employee = baseDao.get(id);
		// 因为此时的 Employee 是一个持久化对象, 所以可以修改其属性, 且能够完成更新操作.
		employee.setIsDeleted(1);
		return 1;
	}

	/*@Transactional(readOnly = true)
	public Employee getByLoginName(String loginName) {
		return baseDao.getBy("loginName", loginName);
	}*/

	@Transactional
	public void save(Employee employee) {
		// employeeId属性为null，说明是添加操作
		if (employee.getEmployeeId() == null) {
			employee.setPassword("123456");
		}
		baseDao.saveOrUpdate(employee);
	}

	/*
	 * @Transactional(readOnly = true) public Employee get(Integer id) {
	 * Employee employee = baseDao.get(id);
	 * 
	 * // System.out.println(employee.getDepartment().getClass().getName());//
	 * // cn.hlj.ems.entities.Department_$$_javassist_3
	 * 
	 * Hibernate.initialize(employee.getDepartment());
	 * Hibernate.initialize(employee.getEditor());
	 * Hibernate.initialize(employee.getRoles());
	 * 
	 * return employee; }
	 */
	
	// 设置子类需要初始化的属性
	@Override
	protected void initializeEntity(Employee entity) {
		Hibernate.initialize(entity.getEditor());
		Hibernate.initialize(entity.getRoles());
		Hibernate.initialize(entity.getDepartment());
	}

	// -----------------------------------------文件下载-----------------------------------------------

	@Transactional(readOnly = true)
	public void downloadFile(String tempFileName) throws IOException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("employees");

		// 读取所有的Employee，把它们添加到Excel文档中
		// 1. 填充 "标题" 行: 序号 登录名 姓名 性别 登录许可 部门 E-mail 角色
		setTitles(sheet);

		// 2. 读取所有的 Employee(也要读取它的 department 和 roles),并把所有
		// 的Employee对象的相应信息 填充到Excel文档中
		setContent(sheet);

		// 3. 设置表格的样式: 行高, 列宽, 边框
		setCellStyle(wb);

		FileOutputStream fileOut = new FileOutputStream(tempFileName);
		wb.write(fileOut);
		fileOut.close();
	}

	private void setContent(Sheet sheet) {
		List<Employee> employees = baseDao.getAll();

		for (int i = 0; i < employees.size(); i++) {
			Employee employee = employees.get(i);

			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue((i + 1) + "");
			row.createCell(1).setCellValue(employee.getLoginName() + "");
			row.createCell(2).setCellValue(employee.getEmployeeName() + "");
			row.createCell(3).setCellValue(employee.getGender() + "");
			row.createCell(4).setCellValue(employee.getEnabled() + "");
			row.createCell(5).setCellValue(employee.getDepartmentName() + "");
			row.createCell(6).setCellValue(employee.getEmail() + "");
			row.createCell(7).setCellValue(employee.getRoleNames() + "");
		}
	}

	private void setCellStyle(Workbook wb) {
		CellStyle cellStyle = getCellStyle(wb);

		Sheet sheet = wb.getSheet("employees");

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			// 设置行高
			Row row = sheet.getRow(i);
			row.setHeightInPoints(30);

			for (int j = 0; j < row.getLastCellNum(); j++) {
				// 设置列宽
				sheet.autoSizeColumn(j);
				sheet.setColumnWidth(j, (int) (sheet.getColumnWidth(j) * 1.5));

				// 设置边框
				row.getCell(j).setCellStyle(cellStyle);
			}
		}
	}

	private CellStyle getCellStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());

		return style;
	}

	private void setTitles(Sheet sheet) {
		Row row = sheet.createRow(0);

		String[] titles = { "序号", "登录名", "姓名", "性别", "登录许可", "部门", "E-mail", "角色" };
		for (int i = 0; i < titles.length; i++) {
			row.createCell(i).setCellValue(titles[i]);
		}
	}

	// -----------------------------------------文件下载-----------------------------------------------

	// -----------------------------------------文件上传-----------------------------------------------

	@Transactional
	public List<String[]> upload(File file) throws Exception {
		List<String[]> positions = new ArrayList<>();

		// 1. 把 File 对象解析为一个 Workbook 对象
		Workbook workbook = WorkbookFactory.create(new FileInputStream(file));

		// 2. 得到 name="employees" 的 Sheet 对象
		Sheet sheet = workbook.getSheet("employees");

		// 3. 把内容解析为一个 Employee 的 List.
		List<Employee> emps = parseSheetToEmployees(sheet, positions);

		if (positions.size() > 0) {
			return positions;
		}

		System.out.println(emps.size() + " records has passed the validation.");
		// 4. 完成批量的上传
		baseDao.batchSave(emps);

		return positions;
	}

	private List<Employee> parseSheetToEmployees(Sheet sheet, List<String[]> positions) {
		List<Employee> emps = new ArrayList<>();

		// Excel文件中的第1行为标题，所以i从1开始
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			// 获取每一行的记录
			Row row = sheet.getRow(i);

			// 把每一行的记录解析为一个Employee对象
			// 若解析的过程没有出错，则把Employee对象添加到List中
			Employee employee = parseRowToEmployee(row, i + 1, positions);
			if (employee != null) {
				emps.add(employee);
			}

		}

		return emps;
	}

	private Employee parseRowToEmployee(Row row, int rowNum, List<String[]> positions) {
		// 1. 获取每一个单元格内的值，并做相应验证，如果验证出错，将出错的位置添加到List里
		// 序号 登录名 姓名 性别 登录许可 部门 E-mail 角色（序号不是需要被解析的数据）

		// 登录名
		Cell cell = row.getCell(1);
		Object cellVal = getCellVal(cell);
		// 要求登录名以大小写英文字母开头，后面有若干位单词字符，且登录名长度要求在6位及其以上
		String loginName = validateLoginName(cellVal, rowNum, positions);
		boolean flag = (loginName != null);

		// 客户姓名
		cell = row.getCell(2);
		cellVal = getCellVal(cell);
		// 要求客户姓名是字符串
		String employeeName = validateEmployeeName(cellVal, rowNum, positions);
		flag = (employeeName != null) && flag;

		// 性别
		cell = row.getCell(3);
		cellVal = getCellVal(cell);
		// 在Excel文件中，填写文本格式的1或0，会被解析为String类型的1或0
		// 而填写数字格式的1或0，都会被解析为浮点型的1.0或0.0
		// System.out.println(cellVal);
		// 要求性别、登录许可的值是1或0
		String gender = validateGenderOrEnabled(cellVal, rowNum, 4, positions);
		flag = (gender != null) && flag;

		// 登录许可
		cell = row.getCell(4);
		cellVal = getCellVal(cell);
		String enabledStr = validateGenderOrEnabled(cellVal, rowNum, 5, positions);
		Integer enabled = (enabledStr == null ? null : Integer.parseInt(enabledStr));
		flag = (enabled != null) && flag;

		// 部门
		cell = row.getCell(5);
		cellVal = getCellVal(cell);
		// 要求部门必须是已经存在的某一个部门的名字
		Department department = validateDepartmentName(cellVal, rowNum, positions);
		flag = (department != null) && flag;

		// Email
		cell = row.getCell(6);
		cellVal = getCellVal(cell);
		// 要求Email是一个合法的 Email 地址
		String email = validateEmail(cellVal, rowNum, positions);
		flag = (email != null) && flag;

		// 角色
		cell = row.getCell(7);
		cellVal = getCellVal(cell);
		// 要求角色必须是已经存在的 Role 的 roleName 的列表(用逗号分隔, 若有一个不对, 都不行)
		Set<Role> roles = validateRoles(cellVal, rowNum, positions);
		flag = (roles != null) && flag;

		// 2.如果通过了所有验证，则将获取到的信息封装为Employee对象
		if (flag) {
			Employee employee = new Employee(loginName, employeeName, roles, enabled, department, gender, email);
			return employee;
		}
		return null;
	}

	// 验证角色
	private Set<Role> validateRoles(Object cellVal, int rowNum, List<String[]> positions) {

		if (cellVal instanceof String) {
			String val = (String) cellVal;
			// 将用户填写的所有中文逗号字符替换为英文格式的
			val = val.replace("，", ",");

			// 用来存储角色值的字符串数组
			String[] splitVals = val.split(",");

			List<Role> roleList = roleDao.getByIn("roleName", Arrays.asList(splitVals));
			// 确保文档里的角色值都是有效的
			if (roleList.size() == splitVals.length) {
				return new HashSet<>(roleList);
			}

		}

		positions.add(new String[] { rowNum + "", "8" });

		return null;
	}

	// 验证Email
	private String validateEmail(Object cellVal, int rowNum, List<String[]> positions) {
		if (cellVal instanceof String) {
			String email = (String) cellVal;
			Pattern pattern = Pattern.compile("^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$");
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				return email;
			}
		}

		positions.add(new String[] { rowNum + "", "7" });
		return null;
	}

	// 验证部门名称
	private Department validateDepartmentName(Object cellVal, int rowNum, List<String[]> positions) {
		if (cellVal instanceof String) {
			String deptName = (String) cellVal;
			Department department = departmentDao.getBy("departmentName", deptName);
			if (department != null) {
				return department;
			}
		}

		positions.add(new String[] { rowNum + "", "6" });
		return null;
	}

	// 验证性别或登录许可
	private String validateGenderOrEnabled(Object cellVal, int rowNum, int colNum, List<String[]> positions) {
		if (cellVal instanceof Double) {
			Double val = (Double) cellVal;
			if (val == 0.0) {
				return "0";
			} else if (val == 1.0) {
				return "1";
			}
		}

		if (cellVal instanceof String) {
			String val = (String) cellVal;
			if ("0".equals(val) || "1".equals(val)) {
				return val;
			}
		}

		positions.add(new String[] { rowNum + "", colNum + "" });

		return null;
	}

	// 验证employeeName
	private String validateEmployeeName(Object cellVal, int rowNum, List<String[]> positions) {

		if (cellVal instanceof String) {
			return (String) cellVal;
		}

		positions.add(new String[] { rowNum + "", "3" });
		return null;
	}

	// 验证loginName
	private String validateLoginName(Object cellVal, int rowNum, List<String[]> positions) {
		String loginName;

		if (cellVal == null || !(cellVal instanceof String)) {
			loginName = null;
		} else {
			loginName = (String) cellVal;

			if (loginName.length() < 6) {
				loginName = null;
			} else {
				Pattern pattern = Pattern.compile("^[a-zA-Z]\\w+\\w$");
				Matcher matcher = pattern.matcher(loginName);
				if (!matcher.matches()) {
					loginName = null;
				}
			}
		}

		if (loginName == null) {
			positions.add(new String[] { rowNum + "", "2" });
		}

		return loginName;
	}

	private Object getCellVal(Cell cell) {
		// System.out.println(cell==null);
		// 先判断单元格内是否有内容
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			// System.out.print("STRING : ");
			return cell.getRichStringCellValue().getString();
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				// System.out.print("NUMERIC : ");
				return cell.getNumericCellValue();
			}
		case Cell.CELL_TYPE_BOOLEAN:
			// System.out.print("BOOLEAN : ");
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		default:
			return null;
		}

	}

	// -----------------------------------------文件上传-----------------------------------------------

	@Transactional
	public void updateVisitedTimes(Integer id) {
		Employee employee = baseDao.get(id);
		employee.setVisitedTimes(employee.getVisitedTimes() + 1);
	}
}
