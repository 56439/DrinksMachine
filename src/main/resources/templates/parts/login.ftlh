<#macro login path>
    <form action="${path}" method="post">
        <div class="form-group">
            <label class="col-sm-2 col-form-label">Логин</label>
            <div class="col-sm-6">
                <input type="text" name="username" class="form-control" placeholder="Введите логин"/>
            </div>

        </div>
        <div class="form-group">
            <label class="col-sm-2 col-form-label">Пароль</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="Введите пароль"/>
            </div>
        </div>
        <div class="form-group">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="col-sm-6">
                <button class="btn btn-primary" type="submit">Войти</button>
            </div>
        </div>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Выход</button>
    </form>
</#macro>