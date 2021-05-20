<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Сотрудники</h5>
    <a class="btn btn-primary" data-toggle="collapse" href="#addStaffCollapse" role="button" aria-expanded="false" aria-controls="addStaffCollapse">
        Добавить
    </a>
    <div class="collapse" id="addStaffCollapse">
        <div class="form-group w-50">
            <form action="/staff" method="post" class="row no-gutters">
                <div class="col">
                    <input type="text" class="form-control mb-2 mt-3" name="firstName" placeholder="Имя" required>
                </div>
                <div class="col">
                    <input type="text" class="form-control mb-2 mt-3" name="secondName" placeholder="Фамилия" required>
                </div>
                <div class="col">
                    <input type="text" class="form-control mb-2 mt-3" name="thirdName" placeholder="Отчество" required>
                </div>
                <div class="col-12">
                    <input type="text" class="form-control mb-2" name="username" placeholder="Логин" required>
                </div>
                <div class="col-12">
                    <input type="text" class="form-control mb-2" name="password" placeholder="Пароль" required>
                </div>
                <div class="col-12">
                    <select class="form-control mb-2" name="cafe" id="addressCafe" required>
                        <#list cafe as c>
                            <option>${c.address}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="role" id="adminRadio" value="admin" onclick="changeAddress('admin')">
                    <label class="form-check-label" for="adminRadio">Администратор</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="role" id="baristaRadio" value="barista" onclick="changeAddress('barista')" checked>
                    <label class="form-check-label" for="baristaRadio">Бариста</label>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button type="submit" class="btn btn-primary">Добавить</button>
            </form>
        </div>
    </div>
    <div class="row mt-3">
        <div class="col">
            <h6>Администраторы</h6>
            <div class="card my-3">
                <ul class="list-group list-group-flush">
                    <#list admin as a>
                        <li class="list-group-item">
                            <div class="row justify-content-center">
                                <div class="col-sm-3">
                                    <b>${a.username}</b>
                                </div>
                                <div class="col">
                                    ${a.secondName} ${a.firstName} ${a.thirdName}
                                </div>
                                <div class="col-auto">
                                    <a class="btn btn-primary" href="/staff/${a.id}">Изменить</a>
                                </div>
                            </div>
                        </li>
                    <#else>
                        No admins :(
                    </#list>
                </ul>
            </div>
        </div>
        <div class="col">
            <h6>Бариста</h6>
            <div class="card my-3">
                <ul class="list-group list-group-flush">
                    <#list barista as b>
                        <li class="list-group-item">
                            <div class="row justify-content-center">
                                <div class="col-sm-3">
                                    <b>${b.username}</b>
                                </div>
                                <div class="col">
                                    ${b.secondName} ${b.firstName} ${b.thirdName}
                                </div>
                                <div class="col-auto">
                                    <a class="btn btn-primary" href="/staff/barista/${b.id}">Изменить</a>
                                </div>
                            </div>
                        </li>
                    <#else>
                        No barista :(
                    </#list>
                </ul>
            </div>
        </div>
    </div>

    <script>
        changeAddress = (role) => {
            let address = document.getElementById('addressCafe');
            address.hidden = role === 'admin';
        }
    </script>
</@c.page>