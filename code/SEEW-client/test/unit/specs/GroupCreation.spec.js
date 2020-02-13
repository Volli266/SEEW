import Vue from 'vue'
import GroupCreation from '@/components/GroupCreation'

describe('GroupCreation.vue', () => {
  it('Gruppenname soll nach mount null sein', () => {
    const vm = new Vue(GroupCreation).$mount()
    expect(vm.groupName).to.equal('')
  })
  it('GruppenFarbe soll green sein', () => {
    const vm = new Vue(GroupCreation).$mount()
    expect(vm.groupColor).to.equal('green')
  })
  it('Gruppenname soll valid sein', () => {
    const vm = new Vue(GroupCreation).$mount()
    expect(vm.validGroupName).to.equal(true)
  })
  it('Wenn Gruppenname leer ist, dann ist validGroupName = false', () => {
    const vm = new Vue(GroupCreation).$mount()
    vm.groupName = ('     ')
    vm.createGroup()
    expect(vm.validGroupName).to.equal(false)
  })
  it('Wenn Gruppenname buchstabe/zahl enthält, dann ist validGroupName = true', () => {
    const vm = new Vue(GroupCreation).$mount()
    vm.groupName = ('Die beste Gruppe')
    vm.createGroup()
    expect(vm.validGroupName).to.equal(true)
  })
})
