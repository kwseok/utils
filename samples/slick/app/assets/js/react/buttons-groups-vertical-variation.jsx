const {ButtonGroup, Button, DropdownButton, MenuItem} = ReactBootstrap;

export const buttonGroupInstance = (
    <ButtonGroup vertical>
        <Button>Button</Button>
        <Button>Button</Button>
        <DropdownButton title='Dropdown'>
            <MenuItem eventKey='1'>Dropdown link</MenuItem>
            <MenuItem eventKey='2'>Dropdown link</MenuItem>
        </DropdownButton>
        <Button>Button</Button>
        <Button>Button</Button>
        <DropdownButton title='Dropdown'>
            <MenuItem eventKey='1'>Dropdown link</MenuItem>
            <MenuItem eventKey='2'>Dropdown link</MenuItem>
        </DropdownButton>
        <DropdownButton title='Dropdown'>
            <MenuItem eventKey='1'>Dropdown link</MenuItem>
            <MenuItem eventKey='2'>Dropdown link</MenuItem>
        </DropdownButton>
    </ButtonGroup>
);

React.render(buttonGroupInstance, document.getElementById("app"));